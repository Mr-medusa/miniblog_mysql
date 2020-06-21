<template>
    <transition name="card" v-on:leave="leave">
        <div class="pad-item percent1" :id="'pad-item-card'+card.id" :data-foo="order"
             v-bind:style="{ zIndex: showMoreAct ? 4 : null}"
             v-bind:class="cardPercent"
        >
            <div class="pad-item-content">
                <div class="pad-custom-content" :id="'pad-custom-content'+card.id">
                    <div class="barrier" :style="myBackground">
                        <div class="header">
                            <div class="title draggable">
                                <span class="word-ellipsis">#{{card.name}}</span>
                            </div>
                            <div class="minus-act" @click="closeCard(card)">
                                <i class="fa fa-times" aria-hidden="true"></i>
                            </div>
                            <div class="checkbox-pen" v-if="batchSelect" @click="checkboxSelect">
                                <div class="checkbox"
                                     v-bind:style="{'backgroundColor': checkMe?'rgb(235, 116, 13)':null}"><i
                                        aria-hidden="true" class="fa fa-check"></i></div>
                            </div>
                        </div>
                        <div class="content">
                            <!--        codemirror        -->
                            <div v-show="card.type !== 'Math'" class="code-mirror-bar-parent">
                                <div class="code-mirror-bar" :id="'codeMirrorCardEditCodeArea'+card.id"></div>
                            </div>
                            <!--         quill             -->
                            <div v-show="card.type==='Math'"
                                 :class="{white:myFontColor==='white','read-or-write':!card.editable}"
                                 class="quill-content">
                                <div :id="'quillCardEditCodeArea'+card.id"></div>
                            </div>
                        </div>
                        <div class="footer">
                            <div class="date">
                                <p><i class="fa fa-calendar" aria-hidden="true"></i>{{myDate}}</p>
                            </div>
                            <div class="operation-group" :style="{'max-width': isChange?'140px':null}">
                                <div class="save-text" v-if="isChange" @click="saveCardContent"><i
                                        class="fa fa-floppy-o" aria-hidden="true"></i></div>
                                <div class="copy-text" @click="copyCode"><i class="fa fa-files-o"
                                                                            aria-hidden="true"></i></div>
                                <div class="move-to-first" @click="moveToFirst"><i class="fa fa-angle-left"
                                                                                   aria-hidden="true"></i></div>
                                <div class="move-to-last" @click="moveToLast"><i class="fa fa-angle-right"
                                                                                 aria-hidden="true"></i></div>
                                <div vue-authorized class="more-act-pen">
                                    <div class="more-act" @click="showMoreAct=!showMoreAct"><i class="fa fa-ellipsis-h"
                                                                                               aria-hidden="true"></i>
                                    </div>
                                    <div class="dialogs" v-if="showMoreAct && !batchSelect">
                                        <div class="edit" @click="editMe">编辑<i class="fa fa-pencil"
                                                                               aria-hidden="true"></i></div>
                                        <div class="update" @click="clickShowMoreAct(card,'updatePadNameAlert')">
                                            修改<i class="fa fa-pencil-square-o" aria-hidden="true"></i></div>
                                        <div class="update" @click="clickShowMoreAct(card,'movePadToAlert')">移动<i
                                                class="fa fa-truck" aria-hidden="true"></i></div>
                                        <div class="delete" @click="clickShowMoreAct(card,'deletePadAlert')">删除<i
                                                class="fa fa-trash-o" aria-hidden="true"></i></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </transition>
</template>

<script>
    import Utils from "../../utils/Utils"
    import FrontConfig from "../../config/FrontConfig"
    import EventHub from "../../utils/EventHub"
    import CardCache from "../../utils/CardCache"
    import PadsService from "../../server/service/PadsService";

    export default {
        name: "Card",
        props: {
            order: Number,
            card: Object,
            closeCardMoreAct: Boolean,
            batchSelect: Boolean,
            batchBasket: Map,
            cardPercent: Object,
        },
        watch: {
            closeCardMoreAct: function () {
                this.showMoreAct = false;
            },
            batchSelect: function () {
                this.checkMe = false;
            },
            'card.type': function () {
                if (this.editorType === 'CodeMirror' && !this.typeIsMath()) {
                    return;
                }
                this.$nextTick(() => {
                    this.clearEditDomObj();
                    this.initEditDomObj()
                    // this.editObj.innerHTML='';
                    this.mallocCardEditor()
                })
            }
        },
        data() {
            return {
                background: FrontConfig.imgs[Math.floor(Math.random() * FrontConfig.imgs.length)],
                myFontColor: null,
                showMoreAct: false,
                checkMe: false,
                isEditable: false,
                isChange: false,
                editObj: null,
                cardEditor: null,
                editorType: null,
                curCursor: null,
                realCard: null,
            }
        },
        computed: {
            myDate() {
                if (this.card.updateTime) {
                    var time = this.card.updateTime.split(" ");
                    time = time[0].split("-");
                    var month = Utils.numberMonthToEnMonth(time[1]);
                    var day = Utils.numberDayToEnOrdinal(time[2]);
                    return day + " " + month + "," + time[0];
                } else {
                    return Utils.simpleDateFormat(new Date());
                }
            },
            myBackground() {
                if (!this.background) {
                    return {
                        border: '1px solid rgba(56, 56, 56, 0.54)'
                    };
                }
                if (this.background.includes("material")) {
                    return {
                        background: Utils.getBackground(this.background) + 'repeat'
                    }
                } else if (this.background.includes("spot")) {
                    if (['1', '2', '4', '5', '6'].find((e) => this.background.lastIndexOf(e))) {
                        this.myFontColor = 'white'
                    }
                    return {
                        backgroundImage: Utils.getBackground(this.background),
                        backgroundSize: '100% 100%'
                    }
                } else {
                    return this.background;
                }
            },
        },
        mounted() {
            // 等待v-html渲染完成
            this.card.isCard = true;
            this.$emit("padProduced", this.card);
            this.initEditDomObj();
            let that = this;
            let card = CardCache.getCard(this.card.id);
            if (card) {
                // 注意这里需要设置content
                this.assignValue(card)
                this.buildCardEditor(that, card);
            } else {
                PadsService.getCard(this.card.id).then(function (res) {
                    let card = res.data;
                    // 注意这里需要设置content
                    that.assignValue(card)
                    that.buildCardEditor(that, card);
                    CardCache.putCard({
                        id: that.card.id,
                        content: card.content,
                        updateTime: card.updateTime,
                        editable: card.editable
                    });
                }).catch(function (err) {
                    console.log(err);
                });
            }


        },
        methods: {
            buildCardEditor(that, card) {
                that.assignValue(card)
                if (that.card.content != null || that.card.editable) {
                    that.mallocCardEditor();
                    if (that.card.editable)
                        that.editMe();
                }
            },
            moveToFirst() {
                EventHub.padCards.move(document.querySelector('#pad-item-card' + this.card.id), 0);
            },
            moveToLast() {
                EventHub.padCards.move(document.querySelector('#pad-item-card' + this.card.id), -1);
            },

            leave: function (el, done) {
                EventHub.padCards.remove(el, {removeElements: true});
                done();
            },
            checkboxSelect() {
                this.checkMe = !this.checkMe;
                if (this.checkMe) {
                    this.batchBasket.set(this.card.id, this.card)
                } else {
                    this.batchBasket.delete(this.card.id);
                }
            },
            checkAllOfBoxes(isChecked) {
                this.checkMe = isChecked;
                this.checkboxSelect();
            },
            cursorActivity(cm) {
                this.curCursor = cm.getCursor();
            },
            copyCode() {
                if (this.cardEditor) {
                    var text = "";
                    if (this.typeIsMath()) {
                        text = this.cardEditor.getText()
                    } else {
                        text = this.cardEditor.getValue();
                    }
                    if (Utils.copyText(text))
                        EventHub.$emit('goTip', ["复制成功", true, 800]);
                }
            },
            saveCardContent() {
                this.isChange = false;
                let text = "";
                if (this.typeIsMath()) {
                    text = this.editObj.innerHTML
                } else {
                    text = this.cardEditor.getValue();
                }
                let date = Utils.simpleDateFormat(new Date())
                let card = {
                    id: this.card.id,
                    content: text,
                    editable: false,
                    updateTime: date
                };
                this.card.content = text;
                this.card.editable = false;
                this.card.updateTime = date;

                this.setReadOrWrite();

                CardCache.putCard(card);

                PadsService.putCard(card).then(function () {
                    EventHub.$emit("goTip", ["保存Card成功!"])
                }).catch(function (err) {
                    console.log(err);
                    EventHub.$emit("goTip", ["保存Card失败!", false])
                });
            },
            editMe() {
                if (!this.cardEditor) {
                    this.mallocCardEditor();
                }
                this.showMoreAct = false;
                this.isChange = true;
                this.card.editable = true;
                this.setReadOrWrite();
            },
            typeIsMath: function () {
                return this.card.type === 'Math';
            },
            initEditDomObj: function () {
                if (this.typeIsMath()) {
                    this.editObj = document.getElementById("quillCardEditCodeArea" + this.card.id);
                } else {
                    this.editObj = document.getElementById("codeMirrorCardEditCodeArea" + this.card.id);
                }
            },
            clearEditDomObj() {
                let dom;
                if (this.typeIsMath()) {
                    dom = document.getElementById("codeMirrorCardEditCodeArea" + this.card.id);
                    dom.innerHTML = '';
                } else {
                    if (this.editorType === 'Quill') {
                        dom = document.getElementById("quillCardEditCodeArea" + this.card.id);
                        dom.parentNode.removeChild(dom.previousSibling)
                        dom.innerHTML = '';
                    }
                }
            },
            setCardFocus() {
                if (this.typeIsMath()) {
                    this.cardEditor.setSelection(this.cardEditor.getLength(), 0);
                    this.cardEditor.focus();
                } else {
                    // 设置光标到编辑器最后一个字符
                    let lastLine = this.cardEditor.lastLine();
                    let lineHandle = this.cardEditor.getLineHandle(lastLine);
                    if (this.curCursor) {
                        this.cardEditor.setCursor(this.curCursor);
                    } else {
                        this.cardEditor.setCursor({line: lastLine, ch: lineHandle.text.length});
                    }
                    this.cardEditor.focus();
                }
            },
            setReadOrWrite() {
                if (this.card.editable) {
                    if (this.typeIsMath()) {
                        this.cardEditor.enable();
                    } else {
                        // this.cardEditor.setOption("readOnly","nocursor");    这样设置不能使用鼠标选中复制了
                        this.cardEditor.setOption("readOnly", false);
                    }
                    this.setCardFocus()
                } else {
                    if (this.typeIsMath()) {
                        this.cardEditor.disable();
                    } else {
                        // this.cardEditor.setOption("readOnly","nocursor");    这样设置不能使用鼠标选中复制了
                        this.cardEditor.setOption("readOnly", true);
                    }
                }
            },
            mallocCardEditor() {
                let that = this;
                if (this.typeIsMath()) {
                    this.cardEditor = Utils.makeQuillEditorForCard(
                        this.editObj,
                        '#toolbar' + this.card.id,
                        this.card.content,
                        EventHub.getMyType(this.card.type),
                        {
                            saveText: that.saveCardContent
                        })
                    this.editorType = 'Quill';
                } else {
                    this.cardEditor = Utils.makeCodeMirror(
                        this.editObj,
                        this.card.content,
                        EventHub.getMyType(this.card.type),
                        {
                            saveText: that.saveCardContent
                        });
                    // this.cardEditor.setOption("readOnly", false);
                    this.cardEditor.on("cursorActivity", this.cursorActivity);
                    EventHub.codeMirrors.set(this.card.id, this.cardEditor);
                    this.editorType = 'CodeMirror';
                }
                this.setReadOrWrite();
            },
            assignValue(card) {
                if (card) {
                    this.card.editable = card.editable;
                    this.card.updateTime = card.updateTime;
                    this.card.content = card.content;
                }
            },

            closeCard(card) {
                this.$emit('closeCard', card);
                card.isCard = false
            },
            clickShowMoreAct(card, cardEvent) {
                this.showMoreAct = false;
                this.$emit(cardEvent, card);
            },
        },
        beforeDestroy: function () {
            if (this.cardEditor && !this.typeIsMath()) {
                this.cardEditor.off("cursorActivity", this.cursorActivity);
                EventHub.codeMirrors.delete(this.card.id);
            }
            EventHub.$off('selectAllOfBoxes', this.checkAllOfBoxes);
            this.card.seat = null;
            this.cardEditor = null;
        },
        created: function () {
            EventHub.$on('selectAllOfBoxes', this.checkAllOfBoxes);
        },
    }
</script>

