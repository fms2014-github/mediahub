export const state = () => ({
    authUser: {
        jwt: '',
    },
})

export const getters = {
    getJwt: (state) => {
        return state.authUser.jwt
    },
}

export const mutations = {
    SET_USER(state, user) {
        state.authUser = user
    },
}

export const actions = {
    async loginapi({ commit }, jwt) {
        try {
            const data = (await this.$loginAxios.post('/auth/setSession', { jwt })).data
            commit('SET_USER', data)
        } catch (error) {
            if (error.response && error.response.status === 401) {
                throw new Error('Bad credentials')
            }
            throw error
        }
    },

    async logoutapi({ commit }) {
        await this.$loginAxios.post('/auth/deleteSession')
        commit('SET_USER', { jwt: '' })
        this.$router.push('/login')
    },
}
