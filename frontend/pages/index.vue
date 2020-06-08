<template>
    <div id="main-page">
        <client-only placeholder="loading...">
            <live-video-slide-bar />
        </client-only>
        <h1>구독 중인 영상들</h1>
        <div v-if="subscriptionList !== []" id="label-video-list">
            <div v-for="item in subscriptionList" id="label-video-list-item" :key="item.id">
                <h2>{{ item.channelName }} 영상</h2>
                <hr />
                <video-form :vlist="item.videoList"></video-form>
            </div>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import liveVideoSlideBar from '~/components/liveVideoSlideBar.vue'
import videoForm from '~/components/main/videoForm.vue'
export default {
    middleware: 'authenticated',
    components: {
        liveVideoSlideBar,
        videoForm,
    },
    data() {
        return {
            subscriptionList: [],
            videoListArr: [],
            channelId: '',
            yChannelId: '',
            tChannelId: '',
            order: 'date',
            orderName: '최신순',
            total: 1,
            streamer: {
                img: '',
                name: '',
                ysubcnt: 0,
                tsubcnt: 0,
                description: '',
                published: '',
                viewCount: '',
                bannerImg: '',
                channelName: '',
            },
            nextPageToken: ' ',
            vData1: [],
            vData2: [],

            provider: '',
            moreVideo: true,
            videoList: [
                {
                    game: null,
                    curator: null,
                    viewCnt: 0,
                },
            ],
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
    mounted() {
        const labels = JSON.parse(localStorage.getItem('labels'))
        for (const i in labels) {
            for (const j in labels[i].channels) {
                this.channelVidoe(labels[i].channels[j].provider, labels[i].channels[j].channelId)
            }
        }
    },
    methods: {
        channelVidoe(provider, channelId) {
            setTimeout(async () => {
                if (provider === 'google') {
                    const list = []
                    const channelInfo = await this.$youtubeApi.youtubeChannelApi(channelId)
                    console.log('qwgrf', channelInfo.data)
                    const searchVideosRes = await this.$youtubeApi.youtubeSearchVideoApi({ channelId, order: 'date' })
                    const idList = []
                    for (var i = 0; i < searchVideosRes.data.items.length; i++) {
                        idList.push(searchVideosRes.data.items[i].id.videoId)
                    }
                    const idStr = idList.join(',')
                    const videoDetailRes = await this.$youtubeApi.youtubeVideosApi(idStr)
                    for (let i = 0; i < videoDetailRes.data.items.length; i++) {
                        const data = {
                            videoId: videoDetailRes.data.items[i].id,
                            title: videoDetailRes.data.items[i].snippet.title,
                            publishedOrigin: videoDetailRes.data.items[i].snippet.publishedAt,
                            published: videoDetailRes.data.items[i].snippet.publishedAt.substring(0, 10),
                            thumbnail: videoDetailRes.data.items[i].snippet.thumbnails.medium.url,
                            provider: 'google',
                            profileImg: channelInfo.data.items[0].snippet.thumbnails.medium.url,
                            viewCnt: this.numChange(videoDetailRes.data.items[i].statistics.viewCount),
                            channelName: channelInfo.data.items[0].snippet.title,
                            channelId,
                        }
                        list.push(data)
                    }
                    this.subscriptionList.push({ channelName: channelInfo.data.items[0].snippet.title, provider, videoList: list })
                } else {
                    const list = []
                    const channelInfo = await this.$twitchApi.twitchChannelApi(
                        channelId,
                        JSON.parse(localStorage.getItem('auth')).find((i) => i.provider === 'twitch').access_token,
                    )
                    console.log('channelInfo', channelInfo)
                    const searchVideosRes = await this.$twitchApi.twitchVideosApi(
                        channelId,
                        JSON.parse(localStorage.getItem('auth')).find((i) => i.provider === 'twitch').access_token,
                    )
                    console.log('searchVideosRes', searchVideosRes)
                    for (let i = 0; i < (searchVideosRes.data.videos.length >= 4 ? 4 : searchVideosRes.data.videos.length); i++) {
                        const data = {
                            videoId: searchVideosRes.data.videos[i]._id,
                            title: searchVideosRes.data.videos[i].title,
                            publishedOrigin: searchVideosRes.data.videos[i].created_at,
                            published: searchVideosRes.data.videos[i].created_at.substring(0, 10),
                            thumbnail: searchVideosRes.data.videos[i].preview.medium,
                            live: 'none',
                            provider: 'twitch',
                            profileImg: channelInfo.data.logo,
                            viewCnt: this.numChange(searchVideosRes.data.videos[i].views) + '회',
                            channelName: channelInfo.data.display_name,
                            channelId,
                            game: searchVideosRes.data.videos[i].game,
                        }
                        list.push(data)
                    }
                    // const clipRes = await this.$twitchApi.twitchClipsByChannelApi(
                    //     channelInfo.data.name,
                    //     JSON.parse(localStorage.getItem('auth')).find((i) => i.provider === 'twitch').access_token,
                    // )
                    // console.log('clipRes', clipRes.data)
                    // for (let i = 0; i < clipRes.data.clips.length >= 2 ? 2 : clipRes.data.clips.length; i++) {
                    //     const data = {
                    //         videoId: clipRes.data.clips[i].slug === undefined ? '' : clipRes.data.clips[i].slug,
                    //         title: clipRes.data.clips[i].title,
                    //         publishedOrigin: clipRes.data.clips[i].created_at,
                    //         published: clipRes.data.clips[i].created_at.substring(0, 10),
                    //         thumbnail: clipRes.data.clips[i].thumbnails.medium,
                    //         live: 'none',
                    //         provider: 'twitch',
                    //         profileImg: channelInfo.data.logo,
                    //         viewCnt: this.numChange(clipRes.data.clips[i].views) + '회',
                    //         channelName: channelInfo.data.display_name,
                    //         channelId,
                    //         game: clipRes.data.clips[i].game,
                    //         curator: clipRes.data.clips[i].curator.name,
                    //     }
                    //     list.push(data)
                    // }
                    this.subscriptionList.push({ channelName: channelInfo.data.display_name, provider, videoList: list })
                }
            }, 500)
        },
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
        ...mapGetters({ jwt: 'login/getJwt' }),
    },
}
</script>

<style lang="scss" scoped>
#router-view {
    display: block;
    width: calc(100% - 72px);
    height: 100%;
    h1 {
        margin-top: 30px !important;
        font: {
            weight: 700;
            size: 1.74rem;
        }
        margin: 0 0 10px 30px;
    }
    #label-video-list {
        margin-top: 30px;
        h2 {
            font: {
                weight: 400;
                size: 1.24rem;
            }
            margin: 0 0 10px 30px;
        }
        hr {
            margin: 0 10px;
        }
    }
}
</style>
