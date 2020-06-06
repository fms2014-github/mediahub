<template>
    <div id="nav-bar">
        <button id="logo" @click="logo">로고</button>
        <button @click="logout"><img src="../assets/icon/logout.png" /></button>
        <button @click="alarmCheck = !alarmCheck">
            <span id="alarm-icon" class="material-icons">
                notifications
            </span>
        </button>
        <button @click="help">
            <span id="help-icon" class="material-icons">
                help
            </span>
        </button>
        <div v-if="reSyncClick" id="reSync">
            <button id="youtube-sync" @click="syncYoutube">
                <img src="../assets/icon/YouTube.png" />
                <span>YouTube</span>
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
                console.log(res.status)
            })
        },
    },
}
</script>

<style lang="scss" scoped>
@import '../assets/commonMixin';
#nav-bar {
    display: flex;
    position: fixed;
    top: 0px;
    justify-content: flex-end;
    align-items: center;
    width: 100%;
    height: $nav-bar-height;
    background-color: white;
    z-index: 9999;
    #logo {
        position: absolute;
        left: 0px;
    }
    button,
    button:active,
    button:visited,
    button:focus,
    button:link {
        color: black;
        text-decoration: none;
        margin: 0px 20px;
        background-color: rgba(0, 0, 0, 0);
        outline: none;
        border-width: 0px;
        cursor: pointer;
    }
    #alarm-icon {
        margin: 0px 20px;
    }
    #help-icon {
        margin-right: 40px;
    }
    button {
        span {
            font-size: 32px;
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
        height: 82px;
        position: absolute;
        top: 60px;
        right: 0px;
        background-color: white;
        border: {
            width: 1px;
            style: solid;
            color: rgb(180, 180, 180);
        }
        #youtube-sync {
            width: 160px;
            height: 52px;
            margin: 0px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translateX(-50%) translateY(-50%);
            background-color: $youtube-color;
            img {
                background-color: rgb(255, 255, 255);
                padding: 8px;
                margin: 0 8px;
                vertical-align: middle;
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
    }
}
img {
    width: 32px;
}
</style>
