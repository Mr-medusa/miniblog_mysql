package red.medusa.miniblog.web.pad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import red.medusa.miniblog.web.pad.bean.Pad;
import red.medusa.miniblog.web.pad.mapper.CardMapper;
import red.medusa.miniblog.web.pad.mapper.PadMapper;
import red.medusa.miniblog.web.pad.vo.PadBox;
import red.medusa.miniblog.web.pad.vo.PadRoot;

import java.util.*;

@Service
@Transactional
public class PadService {

    public static volatile PadRoot padRootCache = null;

    @Autowired
    private PadMapper padMapper;

    @Autowired
    private CardMapper cardMapper;

    /**
     * 构建目录树
     */
    public PadRoot getFolderFromPads() {
        if (PadService.padRootCache != null)
            return PadService.padRootCache;

        PadService.padRootCache = new PadRoot();

        List<Pad> pads = padMapper.findAll();

        if (pads.isEmpty()) {
            return PadService.padRootCache;
        }

        Map<String, Pad> findPadParentNodeMap = new HashMap<>();

        //建立hash表,id->pad
        pads.forEach(pad -> findPadParentNodeMap.put(pad.getId(), pad));

        //构建目录树
        pads.forEach(pad -> {

            String pid = pad.getParentId();

            if (pid != null) {

                // 获取父节点
                Pad padParent = findPadParentNodeMap.get(pid);

                if (padParent.getChildren() == null) {
                    padParent.setChildren(new ArrayList<>());
                }

                padParent.getChildren().add(pad);

            } else {
                PadService.padRootCache.getFolders().add(pad);
            }
        });


        return PadService.padRootCache;
    }


    public ResponseEntity deleteCardAndPadByIds(String[] ids) {

        padMapper.deletedByIds(ids);
        cardMapper.deletedByIds(ids);

        PadService.padRootCache = null;

        this.clear();

        return ResponseEntity.ok("删除成功!");
    }

    public ResponseEntity moveToOther(List<Pad> pads) {

        pads.forEach(pad -> {
            Pad p = padMapper.findById(pad.getId());
            p.setParentId(pad.getParentId());
            p.setOrder(Integer.MAX_VALUE);
            padMapper.update(p);

            this.clear();
        });

        PadService.padRootCache = null;

        return ResponseEntity.ok("移动成功!");
    }

    public ResponseEntity updatePad(List<Pad> pads) {

        pads.forEach(pad -> {
            Pad p = padMapper.findById(pad.getId());
            p.setName(pad.getName());
            p.setType(pad.getType());
            p.setOrder(pad.getOrder());
            p.setIsDefault(pad.getIsDefault());
            padMapper.update(p);
        });

        PadService.padRootCache = null;

        this.clear();

        return ResponseEntity.ok("移动成功!");
    }

    public ResponseEntity<String> addPadToSepciPlace(PadBox padBox) {

        if (padBox.getPad() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("操作失败!");

        Pad pad = padBox.getPad();
        String orderId = padBox.getOrderId();
        String parentId = padBox.getPad().getParentId();

        List<Pad> children = padMapper.findByParentId(parentId);

        // 现拍好顺序
        children.sort(Comparator.comparing(Pad::getOrder));

        boolean find = false;

        for (int i = 0; i < children.size(); i++) {

            Pad child = children.get(i);

//            orderId后一个pad,即新的pad需要插入到它的前面
            if (child.getId().equals(orderId)) {

                // 替换掉它的位置
                pad.setOrder(i);

                padMapper.insert(pad);

                find = true;
            }

            //从新设置序号 (无论是从前往后 还是从后往前都+1)
            child.setOrder(find ? ++i : i);
            padMapper.update(child);

        }

        PadService.padRootCache = null;

        this.clear();

        return ResponseEntity.ok("操作成功!");
    }

    public void clear() {
        PadService.padRootCache = null;
    }

    public Pad findById(String id) {
        return padMapper.findById(id);
    }

    public void putPad(String id, Pad pad) {
        Pad c = padMapper.findById(id);
        if (Objects.isNull(c)) {
            padMapper.insert(pad);
        } else {
            padMapper.update(pad);
        }
        this.clear();
    }

    public Integer deleteById(String id) {
        this.clear();
        return padMapper.delete(id);
    }
}
