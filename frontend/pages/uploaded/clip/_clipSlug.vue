<template>
    <div id="uploaded-page">
        <div id="upload-container">
            <client-only placeholder="loading...">
                <upload-clip v-if="playInfo.play !== ''" :play-info="playInfo" />
            </client-only>
            <sub-button v-if="playInfo.channelId !== ''" :play-info="playInfo" />
            <hr />
            <h1>추천 영상</h1>
            <video-form />
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import uploadClip from '~/components/uploadClip.vue'
import subButton from '~/components/button.vue'
import videoForm from '~/components/main/videoForm.vue'
export default {
    components: {
        uploadClip,
        subButton,
        videoForm,
    },
    asyncData({ params }) {
        const clipSlug = params.clipSlug
        return { clipSlug }
    },
    data: () => {
        return {
            playInfo: {
                kind: 'twitch',
                play: '',
                channelId: '', // 채널id
                // channelId: 'uzuhama', // 비디오name,
                // clipSlug: 'AnnoyingSecretiveSparrowCmonBruh', // clip slug
            },
        }
    },
    async beforeMount() {
        if (localStorage.getItem('auth') !== null) {
            console.log(localStorage.getItem('auth'))
            const temp = JSON.parse(localStorage.getItem('auth'))
            console.log('before::', temp)
            const twitchInfo = temp.find((i) => i.provider === 'twitch')
            if (temp.find((i) => i.provider === 'twitch') !== undefined) {
                const { data } = await this.$backendAxios.twitchTokerRefresh()
                twitchInfo.access_token = data
                console.log(data)
                temp[temp.indexOf(temp.find((i) => i.provider === 'twitch'))] = twitchInfo
                console.log('after::', temp)
            }
        }
    },
    created() {},
    mounted() {
        this.playInfo.play = this.clipSlug
        const fragmentString = window.location.search.replace('?', '')
        // Parse query string to see if page request is coming from OAuth 2.0 server.
        const params = {}
        const regex = /([^&=]+)=([^&]*)/g
        const m = regex.exec(fragmentString)
        this.playInfo.channelId = decodeURIComponent(m[2])
    },
}
</script>

<style lang="scss" scoped>
#router-view {
    display: flex;
    justify-content: center;
    width: calc(100% - 72px);
    height: 100%;

    #upload-container {
        width: 70%;
    }
}
</style>
