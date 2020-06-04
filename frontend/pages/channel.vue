<template>
    <div id="channel">
        <div>
            <figure class="snip1504">
                <img class="bestimg" :src="streamer.bannerImg" alt="bestScore" />
                <figcaption>
                    <div id="profile-descrip">{{ streamer.description }}</div>
                    <div id="profile-info">
                        <span id="profile-pub">가입일 {{ streamer.published }}</span>
                        <span id="profile-videocnt">영상수 {{ streamer.videoCount }}</span>
                    </div>
                </figcaption>
            </figure>
        </div>
        <div id="profile-div">
            <div id="profile">
                <img id="profile-img" :src="streamer.img" />
                <div class="profile-content">
                    <div id="profile-name">{{ streamer.name }}</div>
                    <!-- <div v-if="streamer.ysubcnt != 0" id="subcnt" class="profile-subcnt">유튜브 구독자 {{ streamer.ysubcnt }}명</div>
                    <div v-if="streamer.tsubcnt != 0" id="followcnt" class="profile-subcnt">트위치 팔로워 {{ streamer.tsubcnt }}명</div> -->
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
            <videoForm :vlist="list" />
        </div>
        <p id="more-btn" @click="more()">더보기</p>
    </div>
</template>

<script>
import axios from 'axios'
import videoForm from '@/components/main/videoForm.vue'
export default {
    components: {
        videoForm,
    },
    data: () => {
        return {
            key: 'AIzaSyCcWNyY_KtbSDxlVXgieCK2wjWo2nerdqM',
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
                videoCount: '',
                bannerImg: '',
            },
            nextPageToken: ' ',
            list: [],
            viewCnt: 72915,
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
        this.channelId = 'UCTE8ueKNA54RcO6MgXif2Tg'
        // this.channelId = 'UCs0dIu9USYQnSyPcekI8Y6A'
        // this.channelId = 'UCkuA_gDjISfGgbdp02BUwyQ'
        const cid = this.channelId

        const params = {
            key: this.key,
            part: 'snippet,statistics,brandingSettings',
            id: cid,
        }
        await axios.get(`https://www.googleapis.com/youtube/v3/channels`, { params }).then((res) => {
            console.log(res)
            this.streamer.name = res.data.items[0].snippet.title
            this.streamer.description = res.data.items[0].snippet.description
            this.streamer.published = res.data.items[0].snippet.publishedAt
            this.streamer.img = res.data.items[0].snippet.thumbnails.medium.url
            this.streamer.ysubcnt = res.data.items[0].statistics.subscriberCount
            this.streamer.published = this.streamer.published.substring(0, 10)
            this.streamer.videoCount = res.data.items[0].statistics.videoCount
            this.streamer.bannerImg = res.data.items[0].brandingSettings.image.bannerImageUrl
        })
    },
    async mounted() {
        await axios
            .get(`https://www.googleapis.com/youtube/v3/search`, {
                params: {
                    key: this.key,
                    part: 'snippet',
                    channelId: this.channelId,
                    pageToken: this.nextPageToken,
                    maxResults: 48,
                    order: this.order,
                },
            })
            .then((res) => {
                console.log(res)
                this.total = res.data.pageInfo.totalResults
                this.nextPageToken = res.data.nextPageToken
                for (let i = 0; i < res.data.items.length; i++) {
                    // axios
                    //     .get('https://www.googleapis.com/youtube/v3/videos', {
                    //         params: {
                    //             key: 'AIzaSyA_4PVT4iLvL92YcMpYrxx_905xfsScqlU',
                    //             part: 'snippet,statistics',
                    //             id: res.data.items[i].id.videoId,
                    //         },
                    //     })
                    //     .then((res) => {
                    //         this.viewCnt = res.data.items[0].statistics.viewCount
                    //         console.log(this.viewCnt)
                    //     })
                    //     .catch((err) => {
                    //         console.log(err)
                    //     })
                    this.viewCnt = 75432
                    let cnt = this.viewCnt
                    let vCnt = ''
                    if (cnt >= 10000) {
                        cnt = cnt / 1000
                        cnt = Math.floor(cnt)
                        if (cnt % 10 > 0) {
                            cnt = cnt / 10
                            vCnt += cnt
                        } else {
                            cnt = Math.floor(cnt / 10)
                            vCnt += cnt
                        }
                        vCnt += '만회'
                    } else if (cnt >= 1000) {
                        cnt = cnt / 100
                        cnt = Math.floor(cnt)
                        if (cnt % 10 > 0) {
                            cnt = cnt / 10
                            vCnt += cnt
                        } else {
                            cnt = Math.floor(cnt / 10)
                            vCnt += cnt
                        }
                        vCnt += '천회'
                    } else {
                        vCnt += cnt + '회'
                    }
                    this.viewCnt = vCnt

                    const listTemp = {
                        videoId: res.data.items[i].id.videoId,
                        title: res.data.items[i].snippet.title,
                        published: res.data.items[i].snippet.publishedAt.substring(0, 10),
                        thumbnail: res.data.items[i].snippet.thumbnails.medium.url,
                        live: res.data.items[i].snippet.liveBroadcastContent,
                        provider: 'youtube',
                        profileImg: this.streamer.img,
                        viewCnt: this.viewCnt,
                        channelName: this.streamer.name,
                    }
                    this.list.push(listTemp)
                }
            })
    },
    methods: {
        async more() {
            await axios
                .get(`https://www.googleapis.com/youtube/v3/search`, {
                    params: {
                        key: this.key,
                        part: 'snippet',
                        channelId: this.channelId,
                        pageToken: this.nextPageToken,
                        maxResults: 48,
                        order: this.order,
                    },
                })
                .then((res) => {
                    console.log(res)
                    this.total = res.data.pageInfo.totalResults
                    this.nextPageToken = res.data.nextPageToken
                    for (let i = 0; i < res.data.items.length; i++) {
                        // axios
                        //     .get('https://www.googleapis.com/youtube/v3/videos', {
                        //         params: {
                        //             key: 'AIzaSyA_4PVT4iLvL92YcMpYrxx_905xfsScqlU',
                        //             part: 'snippet,statistics',
                        //             id: res.data.items[i].id.videoId,
                        //         },
                        //     })
                        //     .then((res) => {
                        //         this.viewCnt = res.data.items[0].statistics.viewCount
                        //         console.log(this.viewCnt)
                        //     })
                        //     .catch((err) => {
                        //         console.log(err)
                        //     })
                        this.viewCnt = 75432
                        let cnt = this.viewCnt
                        let vCnt = ''
                        if (cnt >= 10000) {
                            cnt = cnt / 1000
                            cnt = Math.floor(cnt)
                            if (cnt % 10 > 0) {
                                cnt = cnt / 10
                                vCnt += cnt
                            } else {
                                cnt = Math.floor(cnt / 10)
                                vCnt += cnt
                            }
                            vCnt += '만회'
                        } else if (cnt >= 1000) {
                            cnt = cnt / 100
                            cnt = Math.floor(cnt)
                            if (cnt % 10 > 0) {
                                cnt = cnt / 10
                                vCnt += cnt
                            } else {
                                cnt = Math.floor(cnt / 10)
                                vCnt += cnt
                            }
                            vCnt += '천회'
                        } else {
                            vCnt += cnt + '회'
                        }
                        this.viewCnt = vCnt

                        const listTemp = {
                            videoId: res.data.items[i].id.videoId,
                            title: res.data.items[i].snippet.title,
                            published: res.data.items[i].snippet.publishedAt.substring(0, 10),
                            thumbnail: res.data.items[i].snippet.thumbnails.medium.url,
                            live: res.data.items[i].snippet.liveBroadcastContent,
                            provider: 'youtube',
                            profileImg: this.streamer.img,
                            viewCnt: this.viewCnt,
                            channelName: this.streamer.name,
                        }
                        this.list.push(listTemp)
                    }
                })
        },
    },
}
</script>

<style lang="scss" scoped>
#router-view {
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
        min-height: 50px;
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
        color: rgb(121, 121, 121);
    }
    .cnt {
        font-size: 12px;
        font-weight: 600;
    }
    #subcnt {
        margin-right: 7px;
        // margin-bottom: 7px;
        // color: red;
    }
    #followcnt {
        // color: rgb(186, 37, 255);
    }
    #btns-div {
        width: 35vw;
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
    #more-btn {
        margin: 0 5% 20px 0;
        float: right;
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
        font-family: 'Source Sans Pro', sans-serif;
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
