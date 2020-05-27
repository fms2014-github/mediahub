<template>
    <div id="nav-bar">
        <router-link id="logo" to="#">로고</router-link>
        <router-link to="#"><login-icon></login-icon></router-link>
        <span id="alarm-icon" class="material-icons" @click="alarmCheck = !alarmCheck">
            notifications
        </span>
        <router-link to="#">
            <span id="help-icon" class="material-icons">
                help
            </span>
        </router-link>
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
    methods: {},
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
    a,
    a:active,
    a:visited,
    a:link {
        color: black;
        text-decoration: none;
        margin: 0px 20px;
    }
    #alarm-icon {
        margin: 0px 20px;
    }
    #help-icon {
        margin-right: 40px;
    }
    a {
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
