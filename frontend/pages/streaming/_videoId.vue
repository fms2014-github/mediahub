<template>
    <div id="streaming">
        <div id="live-component">
            <client-only placeholder="loading...">
                <live-video :video-id="videoId"></live-video>
            </client-only>
        </div>
        <hr />
        <div v-for="(item, index) in channel" :key="item.id">
            <nuxt-link :to="'/channel/' + provider + ',' + channelId">
                <div :id="index" class="profile-div">
                    <div class="profile">
                        <img class="profile-img" :src="streamer.img" />
                        <div class="profile-content">
                            <div class="profile-name">{{ streamer.name }}</div>
                            <div>
                                <span v-if="streamer.ysubcnt != 0" class="subcnt profile-subcnt"
                                    >구독자 <span class="cnt">{{ streamer.ysubcnt }}명</span></span
                                >
                                <span v-if="streamer.tsubcnt != 0" class="subcnt profile-subcnt"
                                    >팔로워 <span class="cnt">{{ streamer.tsubcnt }}명</span></span
                                >
                            </div>
                        </div>
                    </div>
                </div>
            </nuxt-link>
        </div>

        <!-- <div id="btns-div">
                    <div id="youtube-btn">
                        <sub-button v-if="youtubeButton.channelId !== ''" :play-info="youtubeButton" />
                    </div>
                    <div id="twitch-btn">
                        <sub-button v-if="twitchButton.channelId !== ''" :play-info="twitchButton" />
                    </div>
            </div> -->
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
        }
    },
    async mounted() {
        const info = this.videoId.split(',')
        this.provider = info[0]
        if (this.provider === 'google') {
            console.log('구글')
            this.channelId = (await this.$youtubeApi.youtubeVideosApi(info[1])).data.items[0].snippet.channelId
            console.log(this.channelId)
            const data = (await this.$youtubeApi.youtubeChannelApi(this.channelId)).data.items[0]
            // console.log(this.channelId)
            console.log('유튜브 streamer', data)
            this.streamer.name = data.snippet.title
            this.streamer.description = data.snippet.description
            this.streamer.published = data.snippet.publishedAt.substring(0, 10)
            this.streamer.img = data.snippet.thumbnails.medium.url
            this.streamer.ysubcnt = this.numChange(data.statistics.subscriberCount)
            this.streamer.viewCount = data.statistics.viewCount
            this.streamer.bannerImg = data.brandingSettings.image.bannerTabletExtraHdImageUrl

            this.channel.push(this.streamer)
            try {
                const res = (
                    await this.$backendAxios.getStreamChannel({
                        channelId: 'UC9aktiWNGpSJKLshflga5yw',
                        provider: 'google',
                    })
                ).data
                console.log(res)
                if (res.length > 1) {
                    const i = res.findIndex((i) => i.provider === 'twitch')
                    const streamer = (await this.$twitchApi.twitchChannelApi(res[i].channelId)).data
                    console.log(streamer)
                }
            } catch (error) {}
        } else {
            console.log('트위치')
            this.channelId = info[1]
            try {
                const streamer = (await this.$twitchApi.twitchChannelApi(this.channelId)).data
            } catch (error) {
                console.log('캐치')
            }
            console.log('트위치 streamer', streamer)
            this.streamer.name = streamer.display_name
            this.streamer.channelName = streamer.name
            this.streamer.description = streamer.description
            this.streamer.img = streamer.logo
            this.streamer.tsubcnt = this.numChange(streamer.followers)
            this.streamer.published = streamer.created_at.substring(0, 10)
            this.streamer.viewCount = streamer.views
            this.streamer.bannerImg = streamer.video_banner
            const res = await this.$backendAxios.getStreamChannel({
                channelId: this.channelId,
                provider: 'twitch',
            })
            console.log(res)
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
