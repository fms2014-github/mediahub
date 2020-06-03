<template>
    <div id="nav-bar">
        <button id="logo" @click="logo">로고</button>
        <button @click="login"><login-icon></login-icon></button>
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
        <div v-if="alarmCheck" class="alarm-div">
            <alarm></alarm>
        </div>
    </div>
</template>

<script>
import loginIcon from '~/assets/icon/sign-in.svg?inline'
import alarm from '~/components/main/alarm.vue'
// import logoutIcon from '~/assets/icon/logout.png?inline'

export default {
    components: {
        loginIcon,
        alarm,
        // logoutIcon,
    },

    data: () => {
        return {
            alarmCheck: false,
        }
    },
    mounted() {},
    methods: {
        logo() {
            this.$router.push('/')
        },
        login() {
            this.$router.push('/login')
        },
        help() {
            alert('서비스 준비 중 입니다.\n저희 서비스에 관심을 가져주셔서 감사합니다.')
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
}
svg {
    width: 32px;
}
</style>
