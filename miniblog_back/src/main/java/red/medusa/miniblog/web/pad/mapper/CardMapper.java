package red.medusa.miniblog.web.pad.mapper;

import red.medusa.miniblog.web.common.BaseMapper;
import red.medusa.miniblog.web.pad.bean.Card;

public interface CardMapper  extends BaseMapper<Card, String> {
    void deletedByIds(String[] ids);
}
