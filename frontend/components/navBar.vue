<template>
    <div id="nav-bar">
        <router-link id="logo" to="#">로고</router-link>
        <router-link to="#"><login-icon></login-icon></router-link>
        <!-- <router-link to="#"
            ><span class="material-icons">
                notifications
            </span></router-link
        > -->
        <span class="material-icons" @click="show">
            notifications
        </span>
        <router-link to="#"
            ><span class="material-icons">
                help
            </span></router-link
        >
        <!-- <alarm></alarm> -->
    </div>
</template>

<script>
import loginIcon from '~/assets/icon/sign-in.svg?inline'
import alarm from '~/components/main/alarm.vue'
// import logoutIcon from '~/assets/icon/logout.png?inline'

export default {
    components: {
        loginIcon,
        // alarm,
        // logoutIcon,
    },
    mounted() {},
    methods: {
        show() {
            alert('dd')
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
    a,
    a:active,
    a:visited,
    a:link {
        color: black;
        text-decoration: none;
        margin: 0px 20px;
    }
    a:last-child {
        margin-right: 40px;
    }
    a {
        span {
            font-size: 32px;
        }
    }
}
svg {
    width: 32px;
}
</style>
