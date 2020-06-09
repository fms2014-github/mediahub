<template>
    <div id="live-video">
        <div id="live-slider">
            <div id="button-wrap">
                <button v-if="!(twitchId === '' || youtubeId === '')" id="changeVideo" @click="nextSlide"></button>
            </div>
            <div id="video-wrap">
                <div v-if="!(twitchId === '' || youtubeId === '')" id="back-blind" class="back-slide"></div>
                <youtube v-if="youtubeId !== ''" id="youtube-video" :player-vars="{ autoplay: 1 }" :video-id="youtubeId" @ready="ready" />
                <div v-if="twitchId !== ''" id="twitch-video"></div>
                <live-chat v-if="liveChatId !== null" :live-chat-id="liveChatId" :youtube="youtubeId" :twitch="twitchId"></live-chat>
                <div v-if="liveChatId === null && twitchId !== ''" id="twitch-chat">
                    <iframe
                        :id="twitchId"
                        frameborder="0"
                        scrolling="yes"
                        :src="'https://www.twitch.tv/embed/' + twitchId + '/chat?parent=localhost'"
                        width="300"
                        style="height: 100%;"
                    >
                    </iframe>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import liveChat from '@/components/liveChat.vue'
export default {
    components: {
        liveChat,
    },
    props: {
        videoId: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            twitchId: '',
            youtubeId: '',
            liveChatId: null,
            videoEvent: {
                paused: 0,
            },
        }
    },
    mounted() {
        this.init()
        console.log(this.twitchId !== '' && this.youtubeId !== '')

        console.log(this.videoId.split(',').length)
    },
    methods: {
        async init() {
            if (this.videoId.split(',').length !== 4) {
                if (this.videoId.split(',')[0] === 'twitch') {
                    console.log(this.videoId.split(',')[1])
                    this.twitchId = this.videoId.split(',')[1]
                    setTimeout(() => {
                        const options = {
                            width: 0,
                            height: 0,
                            channel: this.twitchId,
                        }
                        // eslint-disable-next-line no-undef,no-var
                        this.twitchPlayer = new Twitch.Player('twitch-video', options)
                        // eslint-disable-next-line no-undef
                        this.twitchPlayer.addEventListener(Twitch.Player.READY, () => {
                            document.querySelector('#twitch-video iframe').style.width = '100%'
                            document.querySelector('#twitch-video iframe').style.height = '100%'
                        })
                    }, 100)
                } else if (this.videoId.split(',')[0] === 'google') {
                    this.youtubeId = this.videoId.split(',')[1]
                    try {
                        this.liveChatId = (
                            await this.$youtubeApi.youtubeVideosApi(this.youtubeId)
                        ).data.items[0].liveStreamingDetails.activeLiveChatId
                    } catch (err) {}
                    console.log('123qweasdzcx', this.liveChatId)
                }
            } else {
                console.log(this.videoId)
                if (this.videoId.split(',')[2] === 'twitch') {
                    this.twitchId = this.videoId.split(',')[3]
                    console.log('qawsedrf', this.twitchId)
                    setTimeout(() => {
                        const options = {
                            width: 0,
                            height: 0,
                            channel: this.twitchId,
                        }
                        // eslint-disable-next-line no-undef,no-var
                        this.twitchPlayer = new Twitch.Player('twitch-video', options)
                        // eslint-disable-next-line no-undef
                        this.twitchPlayer.addEventListener(Twitch.Player.READY, () => {
                            document.querySelector('#twitch-video iframe').style.width = '100%'
                            this.$emit('load-complete')
                            document.querySelector('#twitch-video iframe').style.height = '100%'
                        })
                        if (this.twitchId !== '' && this.youtubeId !== '') {
                            const youtubeState = document.getElementById('youtube-video').classList
                            const twitchState = document.getElementById('twitch-video').classList
                            youtubeState.add('back-slide')
                            twitchState.add('front-slide')
                        }
                    }, 500)
                }
                if (this.videoId.split(',')[0] === 'google') {
                    this.youtubeId = this.videoId.split(',')[1]
                    try {
                        this.liveChatId = (
                            await this.$youtubeApi.youtubeVideosApi(this.youtubeId)
                        ).data.items[0].liveStreamingDetails.activeLiveChatId
                    } catch (err) {}
                    console.log('123qweasdzcx', this.liveChatId)
                }
            }
        },
        ready(e) {
            this.player = e.target
            this.$emit('load-complete')
            document.querySelector('#youtube-video iframe').style.width = '100%'
            document.querySelector('#youtube-video iframe').style.height = '100%'
        },
        youtubePlay() {
            this.player.playVideo()
        },
        youtubePause() {
            this.player.pauseVideo()
        },
        nextSlide() {
            const youtubeState = document.getElementById('youtube-video').classList
            const twitchState = document.getElementById('twitch-video').classList
            if (youtubeState[0] === 'back-slide') {
                youtubeState.replace('back-slide', 'front-slide')
                this.youtubePlay()
            } else {
                youtubeState.replace('front-slide', 'back-slide')
                this.youtubePause()
            }
            if (twitchState[0] === 'back-slide') {
                twitchState.replace('back-slide', 'front-slide')
                this.twitchPlayer.play()
            } else {
                twitchState.replace('front-slide', 'back-slide')
                this.twitchPlayer.pause()
            }
        },
    },
}
</script>

<style lang="scss" scoped>
@import '~/assets/commonMixin';
#live-video {
    height: 100%;
    #live-slider {
        display: flex;
        padding-right: 100px;
        flex-basis: 100%;
        align-items: center;
        #button-wrap {
            margin: 0 20px;
            width: 60px;
            height: 90px;
            #changeVideo {
                display: inline-block;
                position: relative;
                width: 60px;
                height: 90px;
                top: 50%;
                transform: translateY(-50%);
                padding: 0px;
                margin: 0px;
                background-color: rgba(0, 0, 0, 0);
                border-color: rgba(0, 0, 0, 0);
            }
            #changeVideo::before {
                display: inline-block;
                position: absolute;
                top: 0px;
                background-color: rgb(180, 180, 180);
                content: '';
                padding: 0px;
                width: 8px;
                height: 50px;
                border: {
                    width: 1px;
                    style: solid;
                    color: rgb(180, 180, 180);
                    radius: 8px 8px 0 8px;
                }
                transform: rotate(40deg);
            }
            #changeVideo::after {
                display: inline-block;
                position: absolute;
                top: 34px;
                background-color: rgb(180, 180, 180);
                content: '';
                padding: 0px;
                width: 8px;
                height: 50px;
                border: {
                    width: 1px;
                    style: solid;
                    color: rgb(180, 180, 180);
                    radius: 8px 0 8px 8px;
                }
                transform: rotate(-40deg);
            }
        }
        #video-wrap {
            position: relative;
            width: 100%;
            height: 0;
            padding-bottom: calc(56.25% - 56.25px);
            #youtube-video {
                position: absolute;
                width: 100%;
                height: 100%;
                transition: all 0.5s;
            }
            #twitch-video {
                position: absolute;
                width: 100%;
                height: 100%;
                transition: all 0.5s;
            }
            #back-blind {
                position: absolute;
                width: 100%;
                height: 100%;
                z-index: 2;
                background-color: rgba(0, 0, 0, 0);
            }
            #live-chat,
            #twitch-chat {
                position: absolute;
                margin-left: calc(100% + 70px);
                height: calc(100% - 2px);
            }
        }
    }
}

.back-slide {
    transform: scale(0.9) translateX(-5%);
    left: 0px;
    z-index: 1;
}
.front-slide {
    left: 50px;
    z-index: 4;
}
</style>
