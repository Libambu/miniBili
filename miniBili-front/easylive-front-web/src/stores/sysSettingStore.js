import { defineStore } from 'pinia'

const useSysSettingStore = defineStore("sysSetting", {
    state: () => {
        return {
            sysSetting: {},
        }
    },
    actions: {

        saveSetting(data) {
            this.sysSetting = data;
        }

    }
})
export {
    useSysSettingStore
};
