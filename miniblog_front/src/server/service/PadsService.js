import Vue from "vue"
import Pads from "../dao/Pads"
import Card from "../dao/Card"
import Utils from "../../utils/Utils"

var padsDao = new Pads();
var cardDao = new Card();

export default {
    /****************** pad start *********************/
    put: function (pad) {
        return padsDao.put(this.getNormalPad(pad));
    },
    delete: function (id) {
        return padsDao.delete(id)
    },

    get: function () {
        return padsDao.get();
    },
    /****************** pad end *********************/

    /****************** card start *********************/
    deleteCard(id) {
        return cardDao.delete(id);
    },
    getCard(id) {
        return cardDao.get(id);
    },
    putCard(card) {
        return cardDao.put(this.getNormalCard(card));
    },
    /****************** card end *********************/

    deleteCardAndPadByIds(ids) {
        return Vue.http.post("/deleteCardAndPadByIds", ids);
    },
    moveToOther(pads) {
        return Vue.http.post("/moveToOther", pads);
    },
    updatePad(pads) {
        var that = this;
        var freshPads = [];
        pads.forEach(pad => {
            freshPads.push(that.getFreshPad(pad));
        });
        return Vue.http.post("/updatePad", freshPads);
    },
    putPadToSepciPlace(padBox) {
        padBox.pad = this.getNormalPad(padBox.pad);
        return Vue.http.post("/addPadToSepciPlace", padBox);
    },
    getNormalPad(pad) {
        return {
            id: pad.id,
            parentId: pad.parentId,
            name: pad.name,
            children: Utils.isArrayFn(pad.children) ? [] : null,
            type: pad.type,
            order: pad.order,
            isChecked: pad.isChecked,
            isCard: pad.isCard,
            isShow: pad.isShow,
            isStretch: pad.isStretch,
            createTime: pad.createTime,
            updateTime: pad.updateTime,
            isDefault: pad.isDefault,
        }
    },
    getNormalCard(card) {
        return {
            id: card.id,
            content: card.content,
            updateTime: card.updateTime,
            isChecked: card.isChecked,
            editable: card.editable,
        }
    },
    getFreshPad(pad) {
        return {
            id: pad.id,
            name: pad.name,
            type: pad.type,
            order: pad.order,
            isDefault: pad.isDefault
        }
    }
}

