<template>
    <div id="sidebar">
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
#sidebar {
    display: inline-flex;
    flex-direction: column;
    width: 72px;
    height: calc(100vh - 62px);
    border: {
        width: 0 2px 0 0;
        color: rgb(180, 180, 180);
        style: solid;
    }
    svg {
        width: 42px;
    }
    vertical-align: top;
}
</style>
