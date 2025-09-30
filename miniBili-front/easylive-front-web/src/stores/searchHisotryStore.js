import { defineStore } from 'pinia'
import VueCookies from 'vue-cookies'

const useSearchHistoryStore = defineStore("searchHistoryState", {
    state: () => {
        return {
            searchHistory: [],
        }
    },
    actions: {
        initHistory() {
            this.searchHistory = VueCookies.get('searchHistory') || [];
        },
        addHistory(data) {
            if (this.searchHistory.includes(data)) {
                return;
            }
            if (this.searchHistory.length >= 10) {
                this.searchHistory.pop();
            }
            this.searchHistory.unshift(data);
            VueCookies.set('searchHistory', this.searchHistory);
        },
        delHistory(data) {
            this.searchHistory = this.searchHistory.filter(item => {
                return item != data;
            })
            VueCookies.set('searchHistory', this.searchHistory);
        },
        cleanHistory() {
            this.searchHistory = []
            VueCookies.set('searchHistory', []);
        },
    }
})
export {
    useSearchHistoryStore
};
