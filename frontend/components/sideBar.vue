<template>
    <div id="side-bar">
        <liveBroadcast></liveBroadcast>
        <h4>카테고리</h4>
        <div id="category"></div>
    </div>
</template>

<script>
import liveBroadcast from '@/assets/icon/liveBroadcast.svg?inline'
export default {
    components: {
        liveBroadcast,
    },
    data() {
        return {
            catList: {
                list1: [{ subList11: ['s1', 's2', 's3', 's4'] }, '12', '13', '14'],
                list2: [{ subList21: ['s1', 's2', 's3', 's4'] }, '22', '23', '24'],
                list3: [{ subList31: ['s1', 's2', { ssList: ['ss1', 'ss2', 'ss3'] }, 's4'] }, '32', '33', '34'],
            },
        }
    },
    mounted() {
        const category = document.getElementById('category')
        // console.log(this.catList)
        function subList(parentNode, data) {
            for (const list in data) {
                if (data[list].constructor === Array) {
                    console.log(list)
                    subList(parentNode, data[list])
                } else {
                    console.log(data[list])
                }
            }
        }
        subList(category, this.catList)
    },
}
</script>

<style lang="scss" scoped>
@import '../assets/commonMixin';
#side-bar {
    display: flex;
    position: fixed;
    top: 58px;
    height: 100%;
    flex-direction: column;
    min-width: $side-bar-width;
    background-color: white;
    align-self: stretch;
    z-index: 9999;
    svg {
        width: 42px;
    }
}
</style>
