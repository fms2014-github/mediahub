<template>
    <div id="videoForm-container">
        <div class="video-div">
            <div class="video-list">
                <div v-for="l in vlist" :key="l.videoId" class="video-form" @click="goVideo(l.videoId)">
                    <div class="img-wrap">
                        <img :src="l.thumbnail" alt="" class="img-url" />
                        <img v-if="l.provider == 'youtube'" src="../../assets/icon/youtubeIcon.png" alt="" class="imgIcon" />
                        <img v-if="l.provider == 'twitch'" src="../../assets/icon/twitchIcon.png" alt="" class="imgIcon" />
                    </div>
                    <div class="profile">
                        <div><img class="profileImg" :src="l.profileImg" alt="" /></div>
                        <div class="profileContent">
                            <div class="profileTitle">{{ l.title }}</div>
                            <div class="profileNickname">{{ l.channelName }}</div>
                            <span class="profileHits">조회수 {{ l.viewCnt }}ㆍ</span>
                            <span class="profileDate">{{ l.published }}</span>
                            <img v-if="l.live !== 'none'" src="../../assets/icon/live.png" alt="" class="liveIcon" />
                        </div>
                    </div>
                </div>
                <button @click="go()">리스트test</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        vlist: {
            type: Array,
            default: null,
            // type: Array,
            // default: new Array(),
        },
    },
    data: () => {
        return {
            viewCnt: '',
        }
    },

    created() {},
    methods: {
        go() {
            console.log('gogolist출력')
            console.log(this.vlist)
        },
        goVideo(videoId) {
            alert('비디오아이디:: ' + videoId)
        },
    },
}
</script>

<style lang="scss" scoped>
#videoForm-container {
    .video-list {
        margin: 20px 0;
        display: flex;
        flex-wrap: wrap;
        /* background-color: pink; */
    }
    .video-form {
        width: 21%;
        margin: 0 2%;
        cursor: pointer;
        .img-wrap {
            position: relative;
            width: 100%;
            margin: 3%;
            height: 0;
            padding-bottom: 56.25%;
        }
        .img-url {
            position: absolute;
            width: 100%;
            height: 100%;
        }
        .imgIcon {
            position: absolute;
            width: 30px;
            height: 30px;
            z-index: 10;
            margin: -2px 2px;
        }
    }

    .profile {
        height: 120px;
        display: flex;
        margin: 0 5px 0 0;
        padding: 5px;
    }
    .profileImg {
        width: 40px;
        height: 40px;
        border-radius: 50px;
    }
    .profileContent {
        display: block;
        padding: 5px 0 0 10px;
    }
    .profileTitle {
        height: 35px;
        line-height: 17px;
        overflow: hidden;
        font-weight: 500;
        font-size: 13.6px;
        margin-bottom: 6px;
    }
    .profileTitle:hover {
        overflow-y: scroll;
    }
    .profileNickname {
        font-size: 12px;
        color: rgb(102, 102, 102);
    }
    .profileHits,
    .profileDate {
        font-size: 12px;
        color: rgb(102, 102, 102);
    }
    .liveIcon {
        width: 40px;
        display: block;
    }
}
</style>
