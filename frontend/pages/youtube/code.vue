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
        testaxios
            .post('https://oauth2.googleapis.com/token', {
                code: params.code,
                client_id: '25608544222-lfe7jdkikoef92jgt45mvhe83ts98n80.apps.googleusercontent.com',
                client_secret: 'MjwjwsVlExjXdivmhrIq-CiU',
                redirect_uri: location.origin + '/youtube/code',
                grant_type: 'authorization_code',
            })
            .then(async (res) => {
                console.log(res.data)
                console.log(await testaxios.post('http://k02d1031.p.ssafy.io:8081/v1/youtube/setToken', res.data))
            })
        // const testaxios = this.$axios.create({
        //     headers: { Authorization: 'Bearer ' + this.$store.getters['login/getJwt'] },
        // })
        // console.log('code', await testaxios.get('http://k02d1031.p.ssafy.io:8081/v1/youtube/token-code?code=' + params.code))
        // console.log('synchronization', await testaxios.get('http://k02d1031.p.ssafy.io:8081/v1/youtube/synchronization'))
    },
}
</script>

<style></style>
