<template>
    <div id="channel">
        <div>
            <figure class="snip1504">
                <img v-if="streamer.bannerImg !== null" class="banner-img" :src="streamer.bannerImg" alt="bannerImg" />
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
                    <div id="profile-btns">
                        <button id="sub-btn" class="profile-btn">구독</button>
                        <button id="follow-btn" class="profile-btn">팔로우</button>
                    </div>
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
import videoForm from '@/components/main/videoForm.vue'
export default {
    components: {
        videoForm,
    },
    asyncData({ params }) {
        const channelId = params.channelId
        return { channelId }
    },
    data: () => {
        return {
            // key: 'AIzaSyA_4PVT4iLvL92YcMpYrxx_905xfsScqlU',
            // key: 'AIzaSyCcWNyY_KtbSDxlVXgieCK2wjWo2nerdqM',
            key: 'AIzaSyBc27Pc5zyPqNrwPKnCv7HaV6S8hGa5xDw',
            channelId: '',
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
            },
            nextPageToken: ' ',
            list: [],
            vData1: [],
            vData2: [],
            viewCnt: 0,
            provider: '',
            game: '',
            moreVideo: true,
            idList: [],
            videoList: [
                {
                    videoId: '',
                    title: '',
                    published: '',
                    url: '',
                    live: '',
                    provider: '',
                },
            ],
        }
    },
    async created() {
        const channelInfo = this.channelId.split(',')
        this.provider = channelInfo[0]
        this.channelId = channelInfo[1]
        if (channelInfo.length === 4) {
            this.provider += ',' + channelInfo[2]
            this.channelId += ',' + channelInfo[3]
        }

        if (this.provider === 'google') {
            await axios
                .get(`https://www.googleapis.com/youtube/v3/channels`, {
                    params: {
                        key: this.key,
                        part: 'snippet,statistics,brandingSettings',
                        id: this.channelId,
                    },
                })
                .then((res) => {
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
            this.streamer.name = streamer.display_name // name도 추가할까?
            this.streamer.description = streamer.description
            this.streamer.img = streamer.logo
            this.streamer.tsubcnt = this.numChange(streamer.followers)
            this.streamer.published = streamer.created_at.substring(0, 10)
            this.streamer.viewCount = streamer.views
            this.streamer.bannerImg = streamer.video_banner
        }
    },
    mounted() {
        this.more()
    },
    methods: {
        async more() {
            if (this.provider === 'google') {
                this.vData1 = (
                    await axios.get(`https://www.googleapis.com/youtube/v3/search`, {
                        params: {
                            key: this.key,
                            part: 'snippet',
                            channelId: this.channelId,
                            pageToken: this.nextPageToken,
                            maxResults: 50,
                            order: this.order,
                            type: 'video',
                        },
                    })
                ).data
                this.nextPageToken = this.vData1.nextPageToken
                if (!this.vData1.nextPageToken) {
                    this.moreVideo = false
                }
                for (var i = 0; i < this.vData1.items.length; i++) {
                    this.idList.push(this.vData1.items[i].id.videoId)
                }
                const idStr = this.idList.join(',')
                this.vData2 = (await this.$youtubeApi.youtubeVideosApi(idStr)).data
                console.log(this.vData2.items[0].snippet.publishedAt)
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
                        game: this.game,
                    }
                    this.list.push(data)
                }
            }
            if (this.provider === 'twitch') {
                this.vData1 = (await this.$twitchApi.twitchVideosApi(this.channelId)).data.videos
                for (let i = 0; i < this.vData1.length; i++) {
                    this.game = this.vData1[i].game
                    this.viewCnt = this.numChange(this.vData1[i].views) + '회'
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
                        game: this.game,
                    }
                    this.list.push(data)
                }
            }
            if (this.provider)
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
    // font-family: 'Arita-dotum-Medium';
    font-family: 'S-CoreDream-4Regular';
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
        min-width: 34vw;
        max-width: 35vw;
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
}
</style>
