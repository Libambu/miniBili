import { defineStore } from 'pinia'

const useLoginStore = defineStore("loginState", {
    state: () => {
        return {
            showLogin: false,
            userInfo: {

            },
            messageNoReadCount: 0,
            deviceId: null,
        }
    },
    actions: {
        setLogin(show) {
            this.showLogin = show;
        },
        saveUserInfo(info) {
            this.userInfo = info;
        },
        saveMessageNoReadCount(count) {
            this.messageNoReadCount = count;
        },
        readMessageCount(count) {
            this.messageNoReadCount -= count;
        },
        saveDeviceId(deviceId) {
            this.deviceId = deviceId;
        }
    }
})
export {
    useLoginStore
};
