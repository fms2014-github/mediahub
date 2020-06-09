<template>
    <div id="uploaded-page">
        <div id="upload-container">
            <client-only placeholder="loading...">
                <upload-video v-if="playInfo.play !== ''" :play-info="playInfo" />
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
import uploadVideo from '~/components/uploadVideo.vue'
import subButton from '~/components/button.vue'
import videoForm from '~/components/main/videoForm.vue'
export default {
    components: {
        uploadVideo,
        subButton,
        videoForm,
    },
    asyncData({ params }) {
        const videoId = params.videoId
        return { videoId }
    },
    data: () => {
        return {
            playInfo: {
                kind: 'google',
                play: '',
                channelId: '',
            },
            list: [],
        }
    },
    created() {},
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
    async mounted() {
        // console.log(this.videoId)
        // console.log('youtube', this.playInfo)
        this.playInfo.play = this.videoId
        const fragmentString = window.location.search.replace('?', '')
        // Parse query string to see if page request is coming from OAuth 2.0 server.
        const params = {}
        const regex = /([^&=]+)=([^&]*)/g
        const m = regex.exec(fragmentString)
        this.playInfo.channelId = decodeURIComponent(m[2])

        const streamer = (await this.$youtubeApi.youtubeChannelApi(this.playInfo.channelId)).data.items[0].snippet
        // console.log('streamer', streamer)
        const data = {
            channelId: this.playInfo.channelId,
            pageToken: '',
            order: this.order,
        }
        const vData1 = (await this.$youtubeApi.youtubeSearchVideoApi(data)).data
        const idList = []
        for (var i = 0; i < vData1.items.length; i++) {
            idList.push(vData1.items[i].id.videoId)
        }
        const idStr = idList.join(',')
        const vData2 = (await this.$youtubeApi.youtubeVideosApi(idStr)).data
        for (let i = 0; i < vData2.items.length; i++) {
            const data = {
                videoId: vData2.items[i].id,
                title: vData2.items[i].snippet.title,
                publishedOrigin: vData2.items[i].snippet.publishedAt,
                published: vData2.items[i].snippet.publishedAt.substring(0, 10),
                thumbnail: vData2.items[i].snippet.thumbnails.medium.url,
                live: vData1.items[i].snippet.liveBroadcastContent,
                provider: 'google',
                profileImg: streamer.thumbnails.medium.url,
                viewCnt: this.numChange(vData2.items[i].statistics.viewCount),
                channelName: streamer.title,
                channelId: this.playInfo.channelId,
                // game: this.videoList.game,
                curator: null,
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
