<template>
    <div id="uploaded-page">
        <div id="upload-container">
            <client-only placeholder="loading...">
                <upload-clip v-if="playInfo.play !== ''" :play-info="playInfo" />
            </client-only>
            <sub-button v-if="playInfo.channelId !== ''" :play-info="playInfo" />
            <hr />
            <h1>관련 영상</h1>
            <div id="video-container">
                <video-form v-if="list !== []" :vlist="list" />
            </div>
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
            list: [],
        }
    },
    async beforeMount() {
        if (localStorage.getItem('auth') !== null) {
            // console.log(localStorage.getItem('auth'))
            const temp = JSON.parse(localStorage.getItem('auth'))
            // console.log('before::', temp)
            const twitchInfo = temp.find((i) => i.provider === 'twitch')
            if (temp.find((i) => i.provider === 'twitch') !== undefined) {
                const { data } = await this.$backendAxios.twitchTokerRefresh()
                twitchInfo.access_token = data
                // console.log(data)
                temp[temp.indexOf(temp.find((i) => i.provider === 'twitch'))] = twitchInfo
                // console.log('after::', temp)
            }
        }
    },
    created() {},
    async mounted() {
        this.playInfo.play = this.clipSlug
        const fragmentString = window.location.search.replace('?', '')
        // Parse query string to see if page request is coming from OAuth 2.0 server.
        const params = {}
        const regex = /([^&=]+)=([^&]*)/g
        const m = regex.exec(fragmentString)
        this.playInfo.channelId = decodeURIComponent(m[2])

        const streamer = (
            await this.$twitchApi.twitchChannelApi(
                this.playInfo.channelId,
                JSON.parse(localStorage.getItem('auth')).find((i) => i.provider === 'twitch').access_token,
            )
        ).data
        const vData1 = (
            await this.$twitchApi.twitchVideosApi(
                this.playInfo.channelId,
                JSON.parse(localStorage.getItem('auth')).find((i) => i.provider === 'twitch').access_token,
            )
        ).data.videos
        for (let i = 0; i < vData1.length; i++) {
            const data = {
                videoId: vData1[i]._id,
                title: vData1[i].title,
                publishedOrigin: vData1[i].created_at,
                published: vData1[i].created_at.substring(0, 10),
                thumbnail: vData1[i].preview.medium,
                live: 'none',
                provider: 'twitch',
                profileImg: streamer.logo,
                viewCnt: this.numChange(vData1[i].views) + '회',
                channelName: streamer.name,
                channelId: this.playInfo.channelId,
                game: vData1[i].gamee,
                curator: null,
            }
            this.list.push(data)
        }
        const vData2 = (
            await this.$twitchApi.twitchClipsByChannelApi(
                streamer.name,
                JSON.parse(localStorage.getItem('auth')).find((i) => i.provider === 'twitch').access_token,
            )
        ).data.clips
        for (let i = 0; i < vData2.length; i++) {
            const data = {
                videoId: vData2[i].slug,
                title: vData2[i].title,
                publishedOrigin: vData2[i].created_at,
                published: vData2[i].created_at.substring(0, 10),
                thumbnail: vData2[i].thumbnails.medium,
                live: 'none',
                provider: 'twitch',
                profileImg: streamer.logo,
                viewCnt: this.numChange(vData2[i].views) + '회',
                channelName: streamer.name,
                channelId: this.playInfo.channelId,
                game: vData2[i].game,
                curator: vData2[i].curator.name,
            }
            this.list.push(data)
        }
    },
    methods: {
        numChange(n) {
            let cnt = n
            let nCnt = ''
            if (cnt >= 10000) {
                cnt = Math.floor(cnt / 1000)
                if (cnt % 10 > 0) {
                    cnt = cnt / 10
                } else {
                    cnt = Math.floor(cnt / 10)
                }
                nCnt = cnt + '만'
            } else if (cnt >= 1000) {
                cnt = Math.floor(cnt / 100)
                if (cnt % 10 > 0) {
                    cnt = cnt / 10
                } else {
                    cnt = Math.floor(cnt / 10)
                }
                nCnt = cnt + '천'
            } else {
                nCnt = cnt
            }
            return nCnt
        },
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
