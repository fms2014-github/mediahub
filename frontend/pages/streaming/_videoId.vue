<template>
    <div id="streaming">
        <div id="live-component">
            <client-only placeholder="loading...">
                <live-video v-if="liveId !== ''" :video-id="liveId" @load-complete="loading = true"></live-video>
            </client-only>
            <div v-if="!loading" id="loading-page"></div>
        </div>
        <hr />
        <div v-for="(item, index) in channel" :key="item.id">
            <nuxt-link :to="'/channel/' + info.join(',')">
                <div :id="index" class="profile-div">
                    <div class="profile">
                        <img class="profile-img" :src="item.img" />
                        <div class="profile-content">
                            <div class="profile-name">{{ item.name }}</div>
                            <div>
                                <span v-if="item.ysubcnt != 0" class="subcnt profile-subcnt"
                                    >구독자 <span class="cnt">{{ item.ysubcnt }}명</span></span
                                >
                                <span v-if="item.tsubcnt != 0" class="subcnt profile-subcnt"
                                    >팔로워 <span class="cnt">{{ item.tsubcnt }}명</span></span
                                >
                            </div>
                        </div>
                    </div>
                </div>
            </nuxt-link>
        </div>
    </div>
</template>

<script>
import liveVideo from '@/components/liveVideo.vue'
export default {
    components: {
        liveVideo,
    },
    asyncData({ params }) {
        const videoId = params.videoId
        return { videoId }
    },
    data: () => {
        return {
            provider: '',
            channelId: '',
            channel: [],
            info: [],
            liveId: '',
            loading: false,
        }
    },
    async mounted() {
        this.info = this.videoId.split(',')
        const live = this.videoId.split(',')
        this.provider = this.info[0]
        if (this.provider === 'google') {
            this.channelId = (await this.$youtubeApi.youtubeVideosApi(this.info[1])).data.items[0].snippet.channelId
            const data = (await this.$youtubeApi.youtubeChannelApi(this.channelId)).data.items[0]
            const streamer = {
                name: data.snippet.title,
                img: data.snippet.thumbnails.medium.url,
                ysubcnt: this.numChange(data.statistics.subscriberCount),
                tsubcnt: 0,
                bannerImg: data.brandingSettings.image.bannerTabletExtraHdImageUrl,
            }
            this.channel.push(streamer)
            this.info.splice(1, 1, this.channelId)
            try {
                const res = (
                    await this.$backendAxios.getStreamChannel({
                        channelId: this.channelId,
                        provider: 'google',
                    })
                ).data
                if (res.length > 1) {
                    const i = res.findIndex((i) => i.provider === 'twitch')
                    const data = (await this.$twitchApi.twitchChannelApi(res[i].channelId)).data
                    const streamer = {
                        name: data.display_name,
                        img: data.logo,
                        ysubcnt: 0,
                        tsubcnt: this.numChange(data.followers),
                        bannerImg: data.video_banner,
                    }
                    this.channel.push(streamer)
                    this.info.push('twitch')
                    this.info.push(res[i].channelId)
                    live.push('twitch')
                    live.push(res[i].name)
                }
            } catch (error) {}
            this.liveId = live.join(',')
        } else {
            this.labels = JSON.parse(localStorage.getItem('labels'))
            for (const d of this.labels) {
                const i = d.channels.findIndex((i) => i.name === this.info[1] && i.provider === 'twitch')
                if (i >= 0) {
                    this.channelId = d.channels[i].channelId
                    break
                }
            }

            const data = (await this.$twitchApi.twitchChannelApi(this.channelId)).data
            const streamer = {
                name: data.display_name,
                img: data.logo,
                ysubcnt: 0,
                tsubcnt: this.numChange(data.followers),
                bannerImg: data.video_banner,
            }
            this.channel.push(streamer)
            this.info.splice(1, 1, this.channelId)

            try {
                const res = (
                    await this.$backendAxios.getStreamChannel({
                        channelId: this.videoId.split(',')[1],
                        provider: 'twitch',
                    })
                ).data
                if (res.length > 1) {
                    const i = res.findIndex((i) => i.provider === 'google')
                    const data = (await this.$youtubeApi.youtubeChannelApi(res[i].channelId)).data.items[0]
                    const streamer = {
                        name: data.snippet.title,
                        img: data.snippet.thumbnails.medium.url,
                        ysubcnt: this.numChange(data.statistics.subscriberCount),
                        tsubcnt: 0,
                        bannerImg: data.brandingSettings.image.bannerTabletExtraHdImageUrl,
                    }

                    this.channel.push(streamer)
                    this.info.unshift(res[i].channelId)
                    this.info.unshift('google')

                    const liveInfo = await this.$youtubeApi.youtubuLiveVideoApi(res[i].channelId, res[i].name)
                    if (liveInfo.items.length !== 0) {
                        live.unshift(liveInfo.items[0].id.videoId)
                        live.unshift('google')
                    }
                }
            } catch (error) {}
            this.liveId = live.join(',')
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
    align-items: center;
    flex-wrap: wrap;
    height: calc(100% - 58px);
    #live-component {
        margin: 20px 0px;
        width: 156.25vh;
        padding-right: 300px;
        #loading-page {
        }
    }
    hr {
        width: 100%;
        margin: 1%;
        box-shadow: 0px 0px 2px 1px rgb(180, 180, 180);
        background-color: rgb(180, 180, 180);
        border: {
            width: 0px;
        }
        height: 1px;
        clear: both;
        flex-grow: 100;
    }
    h1 {
        margin: 20px 0 10px 20px;
        font: {
            weight: 800;
            size: 2rem;
        }
        flex-wrap: wrap;
    }
    .profile-div {
        width: 90%;
        height: 200px;
        // margin: 78px 5% 20px 5%;
        margin: 20px 5% 20px 5%;
        background-color: rgb(255, 255, 255);
        box-shadow: 0px 1px 1px 0px rgb(184, 184, 184);
        display: inline-block;

        .profile {
            height: 100px;
            margin: 0 10vw;
            padding-top: 50px;
            display: flex;

            .profile-img {
                width: 100px;
                height: 100px;
                margin-left: 6px;
                border-radius: 50px;
            }
            .profile-content {
                height: 75px;
                min-width: 250px;
                margin: 25px 0 0 40px;

                .profile-name {
                    font-size: 25px;
                    margin-bottom: 15px;
                }

                .profile-subcnt {
                    font-size: 12px;
                    color: rgb(112, 112, 112);
                }

                .subcnt {
                    margin-right: 7px;
                }
            }
        }
    }
}
</style>
