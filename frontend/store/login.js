import { Base64 } from 'js-base64'

export const state = () => ({
    authUser: {
        jwt: '',
        firstLogin: 0,
    },
})

export const getters = {
    getJwt: (state) => {
        return state.authUser.jwt
    },
    getRequireSync: (state) => {
        return state.authUser.firstLogin
    },
}

export const mutations = {
    SET_USER(state, user) {
        console.log('SET_USER', user)
        state.authUser = user
        console.log('state.authUser', state.authUser)
    },
    checkRequireSync(state) {
        state.authUser.firstLogin++
        this.$loginAxios.post('/auth/setSession', { firstLogin: state.authUser.firstLogin })
    },
}

export const actions = {
    async loginapi({ commit }, jwt) {
        const firstLogin = JSON.parse(Base64.decode(jwt.split('.')[1])).firstLogin
        try {
            const data = (await this.$loginAxios.post('/auth/setSession', { jwt, firstLogin })).data
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

    async checkRequireSync({ commit }) {
        await this.$backendAxios.addFirstLogin()
        commit('checkRequireSync')
    },
}
