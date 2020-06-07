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
        const twitchAuth = this.$axios.create({
            headers: { Authorization: 'Bearer ' + this.$store.getters['login/getJwt'] },
        })
        twitchAuth.get('https://k02d1031.p.ssafy.io:8081/v1/twitch/token-code?code=' + params.code).then(async (res) => {
            console.log('code', res.data)
            const { status } = await twitchAuth.get('https://k02d1031.p.ssafy.io:8081/v1/twitch/synchronization')
            if (status === 200) {
                this.checkRequireSync()
                this.$router.push('/subsync')
            }
        })
        // console.log('synchronization', await testaxios.get('http://k02d1031.p.ssafy.io:8081/v1/twitch/synchronization'))
    },
    methods: {
        ...mapActions({ checkRequireSync: 'login/checkRequireSync' }),
    },
}
</script>

<style></style>
