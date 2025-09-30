import { defineStore } from 'pinia'

const useNavAction = defineStore("navAction", {
    state: () => {
        return {
            fixedHeader: true,//头部信息定位
            fixedCategory: true,//分类信息定位
            showHeader: true,//头部信息
            showCategroy: true,//分类信息
            forceFixedHeader: false,//强制显示 固定头部信息
        }
    },
    actions: {
        setFixedHeader(fixedHeader) {
            this.fixedHeader = fixedHeader;
        },
        setFixedCategory(fixedCategory) {
            this.fixedCategory = fixedCategory;
        },
        setShowHeader(showHeader) {
            this.showHeader = showHeader;
        },
        setShowCategory(showCategory) {
            this.showCategory = showCategory;
        },
        setForceFixedHeader(forceFixedHeader) {
            this.forceFixedHeader = forceFixedHeader;
        }

    }
})
export {
    useNavAction
};
