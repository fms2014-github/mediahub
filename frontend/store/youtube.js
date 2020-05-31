export const state = () => ({
    liveChatId: '',
    apiKey: 'AIzaSyBu90FIHQnLKwEzUgeoakyc4zl_rBn7-so',
})

export const getters = {
    getliveChatId(state) {
        return state.liveChatId
    },
}

export const mutations = {
    setLiveChatId(state, id) {
        state.liveChatId = id
    },
}

export const actions = {
    async getLiveChatIdApi({ commit, state }, videoId) {
        console.log(state.apiKey)
        const liveChatId = (
            await this.$youtubeApi.get('videos', {
                params: {
                    key: state.apiKey,
                    part: 'snippet,liveStreamingDetails,statistics',
                    id: videoId,
                },
            })
        ).data.items[0].liveStreamingDetails.activeLiveChatId
        commit('setLiveChatId', liveChatId)
    },
}
