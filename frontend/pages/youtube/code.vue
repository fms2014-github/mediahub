<template>
    <div></div>
</template>

<script>
export default {
    async mounted() {
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
        console.log(
            'code',
            await testaxios.post('https://oauth2.googleapis.com/token', {
                code: params.code,
                client_id: '787158256857-9p3q41kl6ceo1e57tbmbmjtvq8q4nrq4.apps.googleusercontent.com',
                client_secret: 'oskeBE5wU7CpNjBPnVy5H3vo',
                redirect_uri: location.origini + '/youtube/code',
                grant_type: 'authorization_code',
            }),
        )
        // console.log('synchronization', await testaxios.get('http://k02d1031.p.ssafy.io:8081/v1/youtube/synchronization'))
    },
}
</script>

<style></style>
