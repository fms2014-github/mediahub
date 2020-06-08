<template>
    <div id="channel">
        <div>
            <figure class="snip1504">
                <img v-if="streamer.bannerImg" class="banner-img" :src="streamer.bannerImg" alt="bannerImg" />
                <img v-else class="banner-img" src="../../assets/images/banner.png" alt="bannerImg" />
                <figcaption>
                    <div id="profile-descrip">{{ streamer.description }}</div>
                    <div id="profile-info">
                        <span id="profile-pub">가입일 {{ streamer.published }} ㆍ</span>
                        <span id="profile-videocnt">조회수 {{ streamer.viewCount }}</span>
                    </div>
                </figcaption>
            </figure>
        </div>
        <div id="profile-div">
            <div id="profile">
                <img id="profile-img" :src="streamer.img" />
                <div class="profile-content">
                    <div id="profile-name">{{ streamer.name }}</div>
                    <div>
                        <span v-if="streamer.ysubcnt != 0" id="subcnt" class="profile-subcnt"
                            >구독자 <span class="cnt">{{ streamer.ysubcnt }}명</span></span
                        >
                        <span v-if="streamer.tsubcnt != 0" id="followcnt" class="profile-subcnt"
                            >팔로워 <span class="cnt">{{ streamer.tsubcnt }}명</span></span
                        >
                    </div>
                </div>
                <div id="btns-div">
                    <div id="youtube-btn">
                        <sub-button v-if="youtubeButton.channelId !== ''" :play-info="youtubeButton" />
                    </div>
                    <div id="twitch-btn">
                        <sub-button v-if="twitchButton.channelId !== ''" :play-info="twitchButton" />
                    </div>
                    <!-- <div id="profile-btns">
                        <button id="sub-btn" class="profile-btn">구독</button>
                        <button id="follow-btn" class="profile-btn">팔로우</button>
                    </div> -->
                </div>
            </div>
        </div>

        <!-- <div id="select-order">
            <select id="order" v-model="orderName" name="order">
                <option value="최신순" selected>최신순</option>
                <option value="인기순">인기순</option>
            </select>
        </div> -->
        <div id="video-container">
            <videoForm v-if="list !== []" :vlist="list" />
        </div>
        <div v-show="moreVideo" id="more-div">
            <p id="more-btn" @click="more()">더보기</p>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import subButton from '~/components/button.vue'
import videoForm from '@/components/main/videoForm.vue'
export default {
    components: {
        videoForm,
        subButton,
    },
    asyncData({ params }) {
        const channelId = params.channelId
        return { channelId }
    },
    data: () => {
        return {
            // key: 'AIzaSyBc27Pc5zyPqNrwPKnCv7HaV6S8hGa5xDw',
            // key: 'AIzaSyA_4PVT4iLvL92YcMpYrxx_905xfsScqlU',
            // key: 'AIzaSyCcWNyY_KtbSDxlVXgieCK2wjWo2nerdqM',
            key: 'AIzaSyAXdT2gaRi4k8XbxWZgAhxNiTJaQW3BH-4',
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
            list: [],
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
            youtubeButton: {
                kind: 'google',
                channelId: '',
            },
            twitchButton: {
                kind: 'twitch',
                channelId: '',
            },
        }
    },

    async mounted() {
        const channelInfo = this.channelId.split(',')
        const yi = channelInfo.findIndex((i) => i === 'google')
        if (yi >= 0) {
            console.log('channelId/y', channelInfo[yi + 1])
            this.youtubeButton.channelId = channelInfo[yi + 1]
        }
        const ti = channelInfo.findIndex((i) => i === 'twitch')
        if (ti >= 0) {
            console.log('channelId/t', channelInfo[ti + 1])
            this.twitchButton.channelId = channelInfo[ti + 1]
        }
        console.log(channelInfo)
        this.provider = channelInfo[0]
        this.channelId = channelInfo[1]
        if (channelInfo.length === 4) {
            this.yChannelId = this.channelId
            this.tChannelId = channelInfo[3]
            this.provider += ',' + channelInfo[2]

            this.channelId += ',' + channelInfo[3]
        }
        if (this.provider === 'google' || this.provider === 'google,twitch') {
            // const streamer = (await this.$youtubeApi.youtubeChannelApi(this.channelId)).data
            // console.log(streamer)
            // this.streamer.name = streamer.items[0].snippet.title
            // this.streamer.description = streamer.items[0].snippet.description
            // this.streamer.published = streamer.items[0].snippet.publishedAt.substring(0, 10)
            // this.streamer.img = streamer.items[0].snippet.thumbnails.medium.url
            // this.streamer.ysubcnt = this.numChange(streamer.items[0].statistics.subscriberCount)
            // this.streamer.viewCount = streamer.items[0].statistics.viewCount
            // this.streamer.bannerImg = streamer.items[0].brandingSettings.image.bannerTabletExtraHdImageUrl

            await this.$youtubeApi.youtubeChannelApi(this.channelId).then((res) => {
                this.streamer.name = res.data.items[0].snippet.title
                this.streamer.description = res.data.items[0].snippet.description
                this.streamer.published = res.data.items[0].snippet.publishedAt.substring(0, 10)
                this.streamer.img = res.data.items[0].snippet.thumbnails.medium.url
                this.streamer.ysubcnt = this.numChange(res.data.items[0].statistics.subscriberCount)
                this.streamer.viewCount = res.data.items[0].statistics.viewCount
                this.streamer.bannerImg = res.data.items[0].brandingSettings.image.bannerTabletExtraHdImageUrl
            })
        } else {
            const streamer = (await this.$twitchApi.twitchChannelApi(this.channelId)).data
            this.streamer.name = streamer.display_name
            this.streamer.channelName = streamer.name
            this.streamer.description = streamer.description
            this.streamer.img = streamer.logo
            this.streamer.tsubcnt = this.numChange(streamer.followers)
            this.streamer.published = streamer.created_at.substring(0, 10)
            this.streamer.viewCount = streamer.views
            this.streamer.bannerImg = streamer.video_banner
        }
        this.more()
    },
    methods: {
        async more() {
            if (this.provider === 'google' || this.provider === 'google,twitch') {
                if (this.provider === 'google,twitch') this.channelId = this.yChannelId
                const data = {
                    channelId: this.channelId,
                    pageToken: this.nextPageToken,
                    order: this.order,
                    maxResults: 48,
                }
                this.vData1 = (await this.$youtubeApi.youtubeSearchVideoApi(data)).data
                this.nextPageToken = this.vData1.nextPageToken
                if (!this.vData1.nextPageToken) {
                    this.moreVideo = false
                }
                const idList = []
                for (var i = 0; i < this.vData1.items.length; i++) {
                    idList.push(this.vData1.items[i].id.videoId)
                }
                const idStr = idList.join(',')
                this.vData2 = (await this.$youtubeApi.youtubeVideosApi(idStr)).data
                for (let i = 0; i < this.vData2.items.length; i++) {
                    const data = {
                        videoId: this.vData2.items[i].id,
                        title: this.vData2.items[i].snippet.title,
                        publishedOrigin: this.vData2.items[i].snippet.publishedAt,
                        published: this.vData2.items[i].snippet.publishedAt.substring(0, 10),
                        thumbnail: this.vData2.items[i].snippet.thumbnails.medium.url,
                        live: this.vData1.items[i].snippet.liveBroadcastContent,
                        provider: 'google',
                        profileImg: this.streamer.img,
                        viewCnt: this.numChange(this.vData2.items[i].statistics.viewCount),
                        channelName: this.streamer.name,
                        channelId: this.channelId,
                        game: this.videoList.game,
                        curator: this.videoList.curator,
                    }
                    this.list.push(data)
                }
            }
            if (this.provider === 'twitch' || this.provider === 'google,twitch') {
                if (this.provider === 'google,twitch') this.channelId = this.tChannelId
                this.vData1 = (await this.$twitchApi.twitchVideosApi(this.channelId)).data.videos
                for (let i = 0; i < this.vData1.length; i++) {
                    this.videoList.game = ''
                    this.videoList.game = this.vData1[i].game
                    this.videoList.viewCnt = this.numChange(this.vData1[i].views) + '회'
                    const data = {
                        videoId: this.vData1[i]._id,
                        title: this.vData1[i].title,
                        publishedOrigin: this.vData1[i].created_at,
                        published: this.vData1[i].created_at.substring(0, 10),
                        thumbnail: this.vData1[i].preview.medium,
                        live: 'none',
                        provider: 'twitch',
                        profileImg: this.streamer.img,
                        viewCnt: this.viewCnt,
                        channelName: this.streamer.name,
                        channelId: this.channelId,
                        game: this.videoList.game,
                        curator: this.videoList.curator,
                    }
                    this.list.push(data)
                }
                this.vData1 = (await this.$twitchApi.twitchClipsByChannelApi(this.streamer.channelName)).data.clips
                console.log(this.vData1)
                for (let i = 0; i < this.vData1.length; i++) {
                    this.videoList.game = ''
                    this.videoList.game = this.vData1[i].game
                    this.videoList.viewCnt = this.numChange(this.vData1[i].views) + '회'
                    this.videoList.curator = null
                    this.videoList.curator = this.vData1[i].curator.name

                    const data = {
                        videoId: this.vData1[i].slug,
                        title: this.vData1[i].title,
                        publishedOrigin: this.vData1[i].created_at,
                        published: this.vData1[i].created_at.substring(0, 10),
                        thumbnail: this.vData1[i].thumbnails.medium,
                        live: 'none',
                        provider: 'twitch',
                        profileImg: this.streamer.img,
                        viewCnt: this.videoList.viewCnt,
                        channelName: this.streamer.name,
                        channelId: this.channelId,
                        game: this.videoList.game,
                        curator: this.videoList.curator,
                    }
                    this.list.push(data)
                }
                if (this.provider === 'google,twitch')
                    this.list.sort((a, b) => {
                        return Date.parse(b.publishedOrigin) - Date.parse(a.publishedOrigin)
                    })
            }
            if (this.provider === 'google,twitch')
                this.list.sort((a, b) => {
                    return Date.parse(b.publishedOrigin) - Date.parse(a.publishedOrigin)
                })
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
    },
}
</script>

<style lang="scss" scoped>
#router-view {
    font-family: 'Arita-dotum-Medium';
    // font-family: 'S-CoreDream-4Regular';
    // font-family: 'KHNPHU';
    // font-family: 'HCRDotum';
    // font-family: 'YESGothic-Regular';
    // width: calc(100% - $side-bar-width);
    width: calc(100% - 74px);
    #profile-div {
        width: 90%;
        height: 200px;
        // margin: 78px 5% 20px 5%;
        margin: 20px 5% 20px 5%;
        background-color: rgb(255, 255, 255);
        box-shadow: 0px 1px 1px 0px rgb(184, 184, 184);
        display: inline-block;
    }
    #profile {
        height: 100px;
        margin: 0 10vw;
        padding-top: 50px;
        display: flex;
    }
    #profile-img {
        width: 100px;
        height: 100px;
        margin-left: 6px;
        border-radius: 50px;
    }
    .profile-content {
        height: 75px;
        min-width: 250px;
        margin: 25px 0 0 40px;
    }
    #profile-name {
        font-size: 25px;
        margin-bottom: 15px;
    }
    #profile-descrip {
        font-size: 12.5px;
        margin-bottom: 15px;
        width: 800px;
        min-height: 30px;
        max-height: 120px;
        line-height: 20px;
        overflow-y: auto;
    }
    #profile-info {
        margin-top: 20px;
    }
    #profile-pub {
        font-size: 11px;
    }
    #profile-videocnt {
        font-size: 11px;
    }
    .profile-subcnt {
        font-size: 12px;
        color: rgb(112, 112, 112);
    }
    .cnt {
        font-size: 12px;
        // font-weight: 600;
    }
    #subcnt {
        margin-right: 7px;
    }

    #btns-div {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        width: 100%;
    }
    #profile-btns {
        float: right;
        margin: 30px 0 0 0;
    }
    .profile-btn {
        width: 65px;
        height: 30px;
        border: none;
        border-radius: 2px;
        font-size: 12px;
        color: white;
        box-shadow: 0px 1px 2px 0px rgb(110, 110, 110);
        outline: none;
        font-family: 'S-CoreDream-4Regular';
    }
    #sub-btn {
        background-color: red;
        margin: 6px;
    }
    #sub-btn:hover {
        background-color: rgb(141, 0, 0);
    }
    #follow-btn {
        background-color: rgb(186, 37, 255);
        margin: 6px;
    }
    #follow-btn:hover {
        background-color: rgb(77, 17, 105);
    }
    #select-order {
        margin: 20px 7% 0 0;
        float: right;
        select {
            width: 80px;
            height: 22px;
            padding-left: 9px;
            outline: none;
            border: none;
            box-shadow: 0px 1px 1px 0px rgb(184, 184, 184);
        }
    }
    #video-container {
        width: 90%;
        margin: 0 5%;
    }
    #more-div {
        position: relative;
    }
    #more-btn {
        position: absolute;
        display: inline-block;
        right: 0px;
        margin: 0 5% 20px 0;
        // float: right;
        color: black;
        font-size: 15px;
        font-weight: 600;
    }
    #more-btn:hover {
        color: rgb(184, 94, 94);
        font-weight: 700;
    }

    @import url(https://fonts.googleapis.com/css?family=Source+Sans+Pro);
    .snip1504 {
        font-family: 'S-CoreDream-4Regular', sans-serif;

        position: relative;
        overflow: hidden;
        width: 100%;
        color: #000000;
        text-align: center;
        font-size: 16px;
        background-color: #fff;
    }

    .snip1504 * {
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        -webkit-transition: all 0.45s ease;
        transition: all 0.45s ease;
    }

    .snip1504 img {
        vertical-align: top;
        width: 100%;
        backface-visibility: hidden;
    }

    .snip1504 figcaption {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        z-index: 1;
        align-items: center;
        bottom: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    #profile-descrip,
    #profile-info {
        margin: 0;
        opacity: 0;
        letter-spacing: 1px;
    }

    #profile-descrip {
        -webkit-transform: translateY(-100%);
        transform: translateY(-100%);
        text-transform: uppercase;
        font-weight: 400;
    }

    #profile-info {
        color: #888;
        -webkit-transform: translateY(100%);
        transform: translateY(100%);
        margin-top: 30px;
    }

    .snip1504:hover > img,
    .snip1504.hover > img {
        opacity: 0.1;
    }

    .snip1504:hover #profile-info,
    .snip1504.hover #profile-info,
    .snip1504:hover #profile-descrip,
    .snip1504.hover #profile-info {
        -webkit-transform: translateY(0);
        transform: translateY(0);
        opacity: 1;
    }
    #youtube-btn {
        display: inline-flex;
        width: 100px;
        height: 35px;
    }
    #twitch-btn {
        display: inline-flex;
        width: 100px;
        height: 35px;
    }
}
</style>
