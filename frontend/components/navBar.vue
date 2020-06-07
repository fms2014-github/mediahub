<template>
    <div id="nav-bar">
        <button @click="logout"><img src="../assets/icon/logout.png" /></button>
        <button @click="alarmCheck = !alarmCheck">
            <span id="alarm-icon" class="material-icons">
                notifications
            </span>
        </button>
        <button @click="help">
            <img id="sync-icon" src="../assets/icon/sync.png" />
        </button>
        <div v-if="reSyncClick" id="reSync">
            <h3>구독 목록 재 동기화</h3>
            <button id="youtube-sync" @click="syncYoutube">
                <img src="../assets/icon/YouTube.png" />
                <span>YouTube</span>
            </button>
            <button id="tiwtch-sync" @click="syncTwitch">
                <img src="../assets/icon/Twitch.png" />
                <span>Twitch</span>
            </button>
        </div>
        <div v-if="alarmCheck" class="alarm-div">
            <alarm></alarm>
        </div>
    </div>
</template>

<script>
// import loginIcon from '~/assets/icon/sign-in.svg?inline'
import { mapActions } from 'vuex'
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
                // console.log(res.status)
            })
        },
        syncTwitch() {
            this.$backendAxios.twitchSynchronization().then((res) => {
                // console.log(res.status)
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
        color: black;
        text-decoration: none;
        margin: 0px 15px;
        background-color: rgba(0, 0, 0, 0);
        outline: none;
        border-width: 0px;
        cursor: pointer;
    }
    #alarm-icon {
        margin: 0px 12px;
    }
    #sync-icon {
        margin-right: 24px;
    }
    button {
        span {
            font-size: 32px;
        }
        img {
            width: 32px;
        }
    }
    .alarm-div {
        position: absolute;
        top: 58px;
        right: 110px;
        z-index: 10;
    }
    #reSync {
        width: 200px;
        height: 122px;
        position: absolute;
        top: 58px;
        right: 0px;
        background-color: white;
        border: {
            width: 1px;
            style: solid;
            color: rgb(180, 180, 180);
        }
        h3 {
            margin-top: 10px;
            text-align: center;
            font: {
                size: 0.865rem;
                weight: 700;
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
            background-color: $youtube-color;
            img {
                background-color: rgb(255, 255, 255);
                padding: 4px;
                margin: 0 8px;
                vertical-align: middle;
                width: 20px;
            }
            span {
                vertical-align: middle;
                font: {
                    size: 0.9rem;
                    weight: 700;
                }
                color: rgb(230, 230, 230);
            }
            border: {
                width: 0px;
                radius: 10px;
            }
        }
        #tiwtch-sync {
            background-color: $twitch-color;
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
                width: 20px;
            }
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
    }
}
img {
    width: 32px;
}
</style>
