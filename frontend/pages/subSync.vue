<template>
    <div id="subscription-sync">
        <div id="content-wrap">
            <h3 id="title1">미디어 허브에 오신 것을 환영합니다.</h3>
            <h3 id="title2">구독 정보를 동기화하시려면 아래 버튼을 눌러주세요.</h3>
            <div id="button-wrap">
                <button id="youtube-sync" @click="syncYoutube">
                    <img src="../assets/icon/youtubeBtn.png" />
                </button>
                <button id="twitch-sync" @click="syncTwitch">
                    <img src="../assets/icon/twitchBtn.png" />
                </button>
                <button v-if="requireSync == 1" id="late-sync" @click="lateSync">
                    <span>다른 계정은 다음에 할께요</span>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
export default {
    layout: 'cover',
    middleware: 'firstLogin',
    data() {
        return {
            requireSync: 0,
        }
    },
    mounted() {
        window.onkeydown = function() {
            var kcode = event.keyCode
            if (kcode === 8 || kcode === 116) {
                alert('현재 페이지는 새로고침이 불가능합니다.')
                event.returnValue = false
            }
        }
        this.requireSync = this.getRequireSync()
        console.log(this.requireSync)
    },
    methods: {
        ...mapGetters({ getRequireSync: 'login/getRequireSync' }),
        ...mapActions({ checkRequireSync: 'login/checkRequireSync' }),
        syncYoutube() {
            window.location.href =
                'https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/youtube&state=statparameter_passthrough_value&access_type=offline&response_type=code&client_id=412300629283-sd7m25vk2m7uijmt9lfpjkscvq3sr6vv.apps.googleusercontent.com&redirect_uri=http://localhost:3000/youtube/code&prompt=consent'
        },
        syncTwitch() {
            window.location.href =
                'https://id.twitch.tv/oauth2/authorize?client_id=db8sw2xqe82gk1x78mkubkr5xh545p&response_type=code&scope=channel_check_subscription channel_commercial channel_editor channel_feed_edit channel_feed_read channel_read channel_stream channel_subscriptions collections_edit communities_edit communities_moderate openid user_blocks_edit user_blocks_read user_follows_edit user_read user_subscriptions viewing_activity_read&redirect_uri=' +
                location.origin +
                'twitch/code'
        },
        lateSync() {
            this.checkRequireSync()
            this.$router.push('/')
        },
    },
}
</script>

<style lang="scss" scoped>
@import '@/assets/commonMixin.scss';
#subscription-sync {
    font-family: 'Arita-dotum-Medium';
    #content-wrap {
        display: flex;
        flex-direction: column;
        justify-content: center;
        height: 100vh;
        width: 100vw;
        transform: translateY(-10%);
        #title1 {
            text-align: center;
            margin-top: 30px;
            margin-bottom: 10px;
        }
        #title2 {
            text-align: center;
            margin-top: 20px;
            margin-bottom: 6px;
        }
        #button-wrap {
            margin-top: 10px;
            button {
                padding: 0px;
                margin: 0px;
            }
            #youtube-sync {
                width: 250px;
                height: 62px;
                margin: 10px 0 10px 50%;
                transform: translateX(-50%);
                background-color: rgba(255, 0, 0, 0.7);
                outline: none;
                img {
                    width: 170px;
                    height: 50px;

                    margin: 0 8px;
                    vertical-align: middle;
                }

                border: {
                    width: 0px;
                }
            }
            #twitch-sync {
                background-color: rgba(145, 71, 255, 0.7);
                margin: 10px 0 0 50%;
                transform: translateX(-50%);
                width: 250px;
                height: 62px;
                outline: none;
                img {
                    width: 170px;
                    height: 50px;
                    margin: 0 8px;
                    vertical-align: middle;
                }

                border: {
                    width: 0px;
                }
            }
            #late-sync {
                background-color: rgb(180, 180, 180);
                margin: 10px 0 10px 50%;
                transform: translateX(-50%);
                width: 250px;
                height: 62px;
                span {
                    vertical-align: middle;
                    font: {
                        size: 0.9rem;
                        weight: 700;
                    }
                    color: rgb(30, 30, 30);
                }
                border: {
                    width: 0px;
                    radius: 10px;
                }
            }
            #youtube-sync:hover {
                background-color: rgb(190, 0, 0);
            }
            #twitch-sync:hover {
                background-color: rgb(122, 62, 212);
            }
            #late-sync:hover {
                background-color: rgb(150, 150, 150);
            }
        }
    }
}
</style>
