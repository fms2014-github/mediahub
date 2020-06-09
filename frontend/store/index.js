export const state = () => ({
    labelRefresh: true,
})
export const strict = false
export const getters = {
    getLabelRefreshState: (state) => {
        return state.labelRefresh
    },
}

export const mutations = {
    mutateLabelRefreshState(state) {
        state.labelRefresh = !state.labelRefresh
        setTimeout(() => {
            state.labelRefresh = !state.labelRefresh
        }, 300)
    },
}

export const actions = {
    // nuxtServerInit is called by Nuxt.js before server-rendering every page
    nuxtServerInit({ commit }, { req }) {
        if (req.session && req.session.data) {
            commit('login/SET_USER', req.session.data)
        }
    },
}
