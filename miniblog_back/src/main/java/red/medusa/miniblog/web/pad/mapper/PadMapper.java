package red.medusa.miniblog.web.pad.mapper;

import red.medusa.miniblog.web.common.BaseMapper;
import red.medusa.miniblog.web.pad.bean.Pad;

import java.util.List;

public interface PadMapper extends BaseMapper<Pad, String> {

    List<Pad> findByParentId(String parentId);

    void deletedByIds(String[] ids);
}
