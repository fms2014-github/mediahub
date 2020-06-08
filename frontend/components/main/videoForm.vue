<template>
    <div id="videoForm-container">
        <div class="video-div">
            <div class="video-list">
                <div v-for="l in vlist" :key="l.id" class="video-form" @click="goVideo(l, l.provider)">
                    <div class="img-wrap">
                        <img v-if="l.provider === 'google'" id="img-y" :src="l.thumbnail" alt="" class="img-url" />
                        <img v-else-if="l.provider === 'twitch'" id="img-t" :src="l.thumbnail" alt="" class="img-url" />
                        <img v-if="l.provider === 'google'" src="../../assets/icon/youtubeIcon2.png" alt="" class="img-icon" />
                        <img v-else-if="l.provider === 'twitch'" src="../../assets/icon/twitchIcon2.png" alt="" class="img-icon" />
                        <img v-if="l.curator" id="clip" src="../../assets/icon/clip.png" alt="" class="img-icon" />
                    </div>
                    <div class="profile">
                        <div><img class="profile-img" :src="l.profileImg" alt="" @click="goChannel(l.channelId, l.provider)" /></div>
                        <div class="profile-contents">
                            <div class="profile-title">{{ l.title }}</div>
                            <div id="profile-nickname" class="profile-content" @click="goChannel(l.channelId, l.provider)">
                                {{ l.channelName }}
                                <span v-if="l.curator" id="profile-curator" class="profile-content">ㆍ클립제작 {{ l.curator }}</span>
                            </div>
                            <div id="profile-hits-date" class="profile-content">조회수 {{ l.viewCnt }}ㆍ{{ l.published }}</div>
                            <div v-if="l.game !== null" id="profile-game" class="profile-content">{{ l.game }}</div>
                            <img v-if="l.live === 'live'" src="../../assets/icon/live.png" alt="" class="live-icon" />
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
            required: true,
        },
    },
    data: () => {
        return {
            viewCnt: '',
        }
    },
    methods: {
        go() {
            console.log('gogolist출력')
            console.log(this.vlist)
        },
        goVideo(videoId, provider) {
            alert('provider, videoId:: ' + provider + ',' + videoId)
        },
        goChannel(channelId, provider) {
            alert('provider, channelId:: ' + provider + ',' + channelId)
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
            margin: 3% 0;
            height: 0;
            padding-bottom: 56.25%;
        }
        .img-url {
            position: absolute;
            width: 100%;
            height: 100%;
        }
        .img-url:hover {
            width: 98%;
            height: 98%;
        }
        #img-y:hover {
            border: 4px solid #da0000be;
        }
        #img-t:hover {
            border: 4px solid #9147ffef;
        }
        .img-icon {
            position: absolute;
            width: 30px;
            height: 30px;
            z-index: 10;
        }
        #clip {
            position: absolute;
            right: 10px;
            top: -13px;
            // width: 35px;
            // height: 35px;
        }
    }

    .profile {
        height: 120px;
        display: flex;
        padding: 5px;
    }
    .profile-img {
        width: 40px;
        height: 40px;
        border-radius: 50px;
    }
    .profile-contents {
        display: block;
        padding: 5px 0 0 10px;
    }
    .profile-title {
        max-height: 35px;
        min-height: 17px;
        line-height: 17px;
        text-overflow: ellipsis;
        // white-space: nowrap;
        overflow: hidden;
        font-weight: 500;
        font-size: 13.6px;
        margin-bottom: 6px;
    }
    .profile-title:hover {
        overflow-y: scroll;
    }
    .profile-title::-webkit-scrollbar {
        width: 8px;
    }
    .profile-title::-webkit-scrollbar {
        width: 8px;
    }
    .profile-title::-webkit-scrollbar-track {
        background-color: rgba(231, 231, 231, 0.815);
    }
    .profile-title::-webkit-scrollbar-thumb {
        background-color: rgba(219, 202, 202, 0.815);
    }

    .profile-content {
        font-size: 12px;
        color: rgb(102, 102, 102);
        margin-bottom: 2px;
    }
    .live-icon {
        width: 40px;
        display: block;
    }
}
</style>
