<template>
    <div id="nav-bar">
        <button @click="logout">
            <span id="logout-icon" class="material-icons">
                meeting_room
            </span>
        </button>
        <button
            @click="
                alarmCheck = !alarmCheck
                reSyncClick = false
            "
        >
            <span id="alarm-icon" class="material-icons">
                notifications
            </span>
        </button>

        <button @click="help">
            <span id="sync-icon" class="material-icons">
                cached
            </span>
        </button>
        <div v-if="reSyncClick" id="reSync">
            <h3>구독 목록 재 동기화</h3>
            <button id="youtube-sync" @click="syncYoutube">
                <img src="../assets/icon/youtubeBtn.png" />
            </button>
            <button id="tiwtch-sync" @click="syncTwitch">
                <img src="../assets/icon/twitchBtn.png" />
            </button>
        </div>
        <div v-if="alarmCheck" class="alarm-div">
            <alarm></alarm>
        </div>
    </div>
</template>

<script>
// import loginIcon from '~/assets/icon/sign-in.svg?inline'
import { mapActions, mapMutations } from 'vuex'
import alarm from '~/components/main/alarm.vue'
// import logoutIcon from '~/assets/icon/logout.png?inline'

export default {
    components: {
        // loginIcon,
        alarm,
        // logoutIcon,
    },

    data: () => {
        return {
            alarmCheck: false,
            reSyncClick: false,
        }
    },
    mounted() {},
    methods: {
        ...mapActions({ logoutapi: 'login/logoutapi' }),
        ...mapMutations(['mutateLabelRefreshState']),
        logo() {
            this.$router.push('/')
        },
        logout() {
            this.logoutapi()
            this.$router.push('/login')
        },
        help() {
            this.alarmCheck = false
            this.reSyncClick = !this.reSyncClick
        },
        syncYoutube() {
            this.$youtubeApi.synchronization().then((res) => {
                console.log(res.status, location.origin)
                this.reSyncClick = !this.reSyncClick
                this.mutateLabelRefreshState()
            })
        },
        syncTwitch() {
            this.$backendAxios
                .twitchSynchronization()
                .then((res) => {
                    console.log(res.status, location.origin)
                    this.mutateLabelRefreshState()
                    this.reSyncClick = !this.reSyncClick
                    console.log(res.status)
                })
                // eslint-disable-next-line handle-callback-err
                .catch((err) => {
                    location.href =
                        'https://id.twitch.tv/oauth2/authorize?client_id=db8sw2xqe82gk1x78mkubkr5xh545p&response_type=code&scope=channel_check_subscription channel_commercial channel_editor channel_feed_edit channel_feed_read channel_read channel_stream channel_subscriptions collections_edit communities_edit communities_moderate openid user_blocks_edit user_blocks_read user_follows_edit user_read user_subscriptions viewing_activity_read&redirect_uri=http://localhost:3000/twitch/code'
                })
        },
    },
}
</script>

<style lang="scss" scoped>
@import '../assets/commonMixin';
#nav-bar {
    display: flex;
    position: relative;
    top: 0px;
    justify-content: flex-end;
    align-items: center;
    width: 100%;
    min-width: calc(1280px);
    height: $nav-bar-height;
    background-color: white;
    z-index: 9998;
    button,
    button:active,
    button:visited,
    button:focus,
    button:link {
        color: rgb(17, 17, 17);
        text-decoration: none;
        margin: 0px 15px;
        background-color: rgba(0, 0, 0, 0);
        outline: none;
        border-width: 0px;
        cursor: pointer;
    }
    #sync-icon {
        margin-right: 50px;
    }
    button {
        span {
            padding-top: 5px;
        }
    }
    .alarm-div {
        position: absolute;
        top: 58px;
        right: 110px;
        z-index: 10;
    }
    #reSync {
        width: 190px;
        height: 122px;
        position: absolute;
        top: 58px;
        right: 5px;
        background-color: rgba(255, 255, 255, 0.863);
        box-shadow: 0 1px 2px rgb(122, 122, 122);
        // border: {
        //     width: 1px;
        //     style: solid;
        //     color: rgb(180, 180, 180);
        // }
        h3 {
            margin-top: 11px;
            text-align: center;
            font: {
                size: 0.865rem;
            }
        }
        #youtube-sync {
            width: 130px;
            height: 32px;
            margin: 0px;
            position: absolute;
            top: calc(50% - 10px);
            left: 50%;
            transform: translateX(-50%) translateY(-50%);
            background-color: rgba(255, 0, 0, 0.7);
            img {
                margin: 0 8px;
                vertical-align: middle;
                width: 100px;
            }

            border: {
                width: 0px;
            }
        }
        #tiwtch-sync {
            background-color: rgba(145, 71, 255, 0.7);
            margin: 0px;
            position: absolute;
            top: calc(50% + 30px);
            left: 50%;
            transform: translateX(-50%) translateY(-50%);
            width: 130px;
            height: 32px;
            img {
                margin: 0 8px;
                vertical-align: middle;
                width: 100px;
            }

            border: {
                width: 0px;
            }
        }
    }
}
</style>
