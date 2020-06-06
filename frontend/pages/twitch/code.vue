<template>
    <div></div>
</template>

<script>
export default {
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
        testaxios.get('https://k02d1031.p.ssafy.io:8081/v1/twitch/token-code?code=' + params.code).then(async (res) => {
            console.log('code', res.data)
            const { data } = await testaxios.get('https://k02d1031.p.ssafy.io:8081/v1/twitch/synchronization')
            console.log('twitch code', data)
            this.$router.push('/')
        })
        // console.log('synchronization', await testaxios.get('https://k02d1031.p.ssafy.io:8081/v1/twitch/synchronization'))
    },
}
</script>

<style></style>
