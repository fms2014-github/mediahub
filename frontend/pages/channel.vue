<template>
    <div id="channel">
        <div id="profile-div">
            <div id="profile">
                <img id="profile-img" src="../assets/images/mainHeader/profile2.png" />
                <div class="profile-content">
                    <div id="profile-name">{{ streamer.name }}</div>
                    <!-- <div v-if="streamer.ysubcnt != 0" id="subcnt" class="profile-subcnt">유튜브 구독자 {{ streamer.ysubcnt }}명</div>
                    <div v-if="streamer.tsubcnt != 0" id="followcnt" class="profile-subcnt">트위치 팔로워 {{ streamer.tsubcnt }}명</div> -->
                    <span v-if="streamer.ysubcnt != 0" id="subcnt" class="profile-subcnt"
                        >구독자 <span class="cnt">{{ streamer.ysubcnt }}명</span></span
                    >
                    <span v-if="streamer.tsubcnt != 0" id="followcnt" class="profile-subcnt"
                        >팔로워 <span class="cnt">{{ streamer.tsubcnt }}명</span></span
                    >
                </div>
                <div id="btns-div">
                    <div id="profile-btns">
                        <button id="sub-btn" class="profile-btn">구독</button>
                        <button id="follow-btn" class="profile-btn">팔로우</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="select-order">
            <select id="order" v-model="order" name="order">
                <option value="최신순" selected>최신순</option>
                <option value="인기순">인기순</option>
            </select>
        </div>
        <div>
            <videoForm />
        </div>
    </div>
</template>

<script>
import videoForm from '@/components/main/videoForm.vue'

export default {
    components: {
        videoForm,
    },
    data: () => {
        return {
            order: '최신순',
            streamer: {
                img: '../assets/images/mainHeader/profile2.png',
                name: '히샨',
                ysubcnt: 300,
                tsubcnt: 100,
            },
        }
    },
}
</script>

<style lang="scss" scoped>
#router-view {
    width: calc(100% - 74px);
    #profile-div {
        width: 90%;
        height: 200px;
        margin: 78px 5%;
        background-color: rgb(196, 173, 197);
        display: inline-block;
    }
    #profile {
        height: 100px;
        margin: 0 10vw;
        padding-top: 50px;
        display: flex;
    }
    #profile-img {
        width: 100px;
        height: 100px;
        margin-left: 6px;
    }
    .profile-content {
        height: 75px;
        min-width: 150px;
        margin: 25px 0 0 40px;
    }
    #profile-name {
        font-size: 25px;
        margin-bottom: 15px;
    }
    .profile-subcnt {
        font-size: 11px;
        color: rgb(121, 121, 121);
    }
    .cnt {
        font-size: 11px;
        font-weight: 600;
    }
    #subcnt {
        margin-right: 7px;
        // margin-bottom: 7px;
        // color: red;
    }
    #followcnt {
        // color: rgb(186, 37, 255);
    }
    #btns-div {
        width: 55vw;
    }
    #profile-btns {
        float: right;
        margin: 30px 0 0 0;
    }
    .profile-btn {
        width: 65px;
        height: 30px;
        border: none;
        border-radius: 2px;
        font-size: 12px;
        color: white;
        box-shadow: 0px 1px 2px 0px rgb(110, 110, 110);
        outline: none;
    }
    #sub-btn {
        background-color: red;
        margin: 6px;
    }
    #sub-btn:hover {
        background-color: rgb(141, 0, 0);
    }
    #follow-btn {
        background-color: rgb(186, 37, 255);
        margin: 6px;
    }
    #follow-btn:hover {
        background-color: rgb(77, 17, 105);
    }
    #select-order {
        margin: 20px 7% 0 0;
        float: right;
        select {
            width: 80px;
            height: 22px;
            padding-left: 9px;
            outline: none;
        }
    }
}
</style>
