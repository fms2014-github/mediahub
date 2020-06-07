<template>
    <div></div>
</template>

<script>
import { mapActions } from 'vuex'
export default {
    layout: 'cover',
    mounted() {
        const fragmentString = window.location.search.replace('?', '')
        // Parse query string to see if page request is coming from OAuth 2.0 server.
        const params = {}
        const regex = /([^&=]+)=([^&]*)/g
        let m
        while ((m = regex.exec(fragmentString))) {
            params[decodeURIComponent(m[1])] = decodeURIComponent(m[2])
        }
        const testaxios = this.$axios.create({
            headers: { Authorization: 'Bearer ' + this.$store.getters['login/getJwt'] },
        })
        testaxios
            .post('https://oauth2.googleapis.com/token', {
                code: params.code,
                client_id: '412300629283-sd7m25vk2m7uijmt9lfpjkscvq3sr6vv.apps.googleusercontent.com',
                client_secret: 'XHhA4GlqOVJZkVmhHHuP1uxj',
                redirect_uri: location.origin + '/youtube/code',
                grant_type: 'authorization_code',
            })
            .then(async (res) => {
                console.log(res)
                const { status } = await testaxios.post('https://k02d1031.p.ssafy.io:8081/v1/youtube/setToken', {
                    accessToken: res.data.access_token,
                    expiresIn: res.data.expires_in,
                    refreshToken: res.data.refresh_token,
                    scope: res.data.scope,
                    tokenType: res.data.token_type,
                })
                if (status === 200) {
                    const { status } = await this.$backendAxios.addFirstLogin()
                    if (status === 200) {
                        this.checkRequireSync()
                        this.$router.push('/subsync')
                    }
                }
            })
        // const testaxios = this.$axios.create({
        //     headers: { Authorization: 'Bearer ' + this.$store.getters['login/getJwt'] },
        // })
        // console.log('code', await testaxios.get('https://k02d1031.p.ssafy.io:8081/v1/youtube/token-code?code=' + params.code))
        // console.log('synchronization', await testaxios.get('https://k02d1031.p.ssafy.io:8081/v1/youtube/synchronization'))
    },
    methods: {
        ...mapActions({ checkRequireSync: 'login/checkRequireSync' }),
    },
}
</script>

<style></style>
