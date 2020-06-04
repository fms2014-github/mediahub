<template>
    <div id="side-bar">
        <liveBroadcast></liveBroadcast>
        <div id="tree"></div>
    </div>
</template>

<script>
import '../assets/label.scss'
import { mapGetters } from 'vuex'
import liveBroadcast from '@/assets/icon/liveBroadcast.svg?inline'
const axios = require('axios')

function dropEvent1(e) {
    e.preventDefault()
    e.stopPropagation()

    console.log('drop1target', e.target)
    console.log('drop', e.target.children[1])
    const channelId = e.dataTransfer.getData('targetId')
    e.target.children[1].appendChild(document.querySelector('div[data-channel-id="' + channelId + '"]'))
    const labelId = e.target.children[1].dataset.labelId
    axios.put(`https://k02d1031.p.ssafy.io:8081/v1/member/channel?channelId=${channelId}&labelId=${labelId}`).then((res) => {
        console.log(res.status)
    })
}
function dropEvent2(e) {
    e.preventDefault()
    e.stopPropagation()
    console.log('drop2target', e.target)
    console.log('drop2', e.target.parentNode)
    e.target.removeEventListener('drop', dropEvent1)
    // node.parentNode.removeEventListener('drop', dropEvent1)
    const channelId = e.dataTransfer.getData('targetId')
    e.target.parentNode.appendChild(document.querySelector('div[data-channel-id="' + channelId + '"]'))
    const labelId = e.target.parentNode.dataset.labelId
    axios.put(`https://k02d1031.p.ssafy.io:8081/v1/member/channel?channelId=${channelId}&labelId=${labelId}`).then((res) => {
        console.log(res.status)
    })
}
export default {
    components: {
        liveBroadcast,
    },
    data() {
        return {
            labels: null,
        }
    },
    async mounted() {
        console.log('jwt', this.getJwt())
        const data1 = (
            await this.$axios.get('https://k02d1031.p.ssafy.io:8081/v1/member/information', {
                headers: { Authorization: 'Bearer ' + this.getJwt() },
            })
        ).data
        console.log(data1)
        this.labels = data1.label
        const data = this.labels
        for (const i in data) {
            // console.log('id', data[i].id)
            // console.log('memberId', data[i].memberId)
            // console.log('labelName', data[i].labelName)
            // console.log('superId', data[i].superId)
            const tree = document.getElementById('tree')
            const node = document.createElement('div')
            node.setAttribute('data-label-id', data[i].id)
            node.setAttribute('data-super-id', data[i].superId)
            node.setAttribute('data-member-id', data[i].memberId)
            const button1 = document.createElement('button')
            const button2 = document.createElement('button')
            button1.setAttribute('class', 'add-child-label')
            button1.appendChild(document.createTextNode('+'))
            button1.onclick = () => {
                const name = prompt('라벨 이름을 적어주세요')
                console.log(name)
                if (name !== null) {
                    this.$axios
                        .post(
                            'https://k02d1031.p.ssafy.io:8081/v1/member/label?labelId=' +
                                button1.parentNode.parentNode.dataset.labelId +
                                '&labelName=' +
                                name,
                            {},
                            { headers: { Authorization: 'Bearer ' + this.getJwt() } },
                        )
                        .then((res) => {
                            console.log(res.status)
                        })
                }
            }
            button2.setAttribute('class', 'delete-child-label')
            button2.appendChild(document.createTextNode('-'))
            button2.onclick = () => {
                const labelId = button2.parentNode.parentNode.dataset.labelId
                if (name !== null) {
                    this.$axios
                        .delete(
                            'https://k02d1031.p.ssafy.io:8081/v1/member/label?labelId=' + labelId + '&labelName=' + name,
                            {},
                            { headers: { Authorization: 'Bearer ' + this.getJwt() } },
                        )
                        .then((res) => {
                            console.log(res.status)
                        })
                }
            }
            if (data[i].superId === -1) {
                node.setAttribute('id', 'label-wrap')
                const span = document.createElement('span')

                span.appendChild(document.createTextNode('카테고리'))
                span.appendChild(button1)
                span.setAttribute('id', 'root-label')
                span.setAttribute('class', 'label-title')
                node.appendChild(span)
                tree.appendChild(node)
            } else {
                const parentLabel = document.querySelector(`div[data-label-id='${data[i].superId}']`)
                if (data[i].superId === 1) {
                    const span1 = document.createElement('span')
                    const span2 = document.createElement('span')
                    const dropCap = document.createElement('div')
                    const dropCapWrap = document.createElement('div')
                    const hr = document.createElement('hr')
                    dropCapWrap.setAttribute('class', 'drop-cap-wrap')
                    dropCapWrap.setAttribute('droppable', 'true')
                    dropCap.setAttribute('class', 'drop-cap')
                    span1.setAttribute('class', 'drop-cap-char')
                    span1.appendChild(document.createTextNode(data[i].labelName[0]))
                    dropCap.appendChild(span1)

                    span2.appendChild(document.createTextNode(data[i].labelName))
                    span2.setAttribute('class', 'label-title')
                    span2.appendChild(button1)
                    span2.appendChild(button2)
                    span2.setAttribute('droppable', 'true')
                    node.appendChild(span2)
                    dropCapWrap.appendChild(dropCap)
                    dropCapWrap.appendChild(node)
                    node.setAttribute('class', 'child-label')
                    parentLabel.insertBefore(dropCapWrap, document.querySelector('.channel'))
                    dropCapWrap.addEventListener('drop', dropEvent1)
                    span2.addEventListener('drop', dropEvent2)
                    dropCapWrap.addEventListener('dragover', (e) => {
                        e.preventDefault()
                    })
                } else {
                    const span = document.createElement('span')
                    const hr = document.createElement('hr')
                    span.appendChild(document.createTextNode(data[i].labelName))
                    span.setAttribute('class', 'label-title')
                    span.appendChild(button1)
                    span.appendChild(button2)
                    span.setAttribute('droppable', 'false')
                    node.appendChild(span)
                    node.setAttribute('class', 'child-label2')
                    parentLabel.insertBefore(node, document.querySelector(`div[data-label-id='${data[i].superId}']` + ' .channel'))
                    node.setAttribute('droppable', 'true')
                    span.addEventListener('drop', dropEvent2)
                    span.addEventListener('dragover', (e) => {
                        e.preventDefault()
                    })
                }
            }
            for (const k in data[i].channels) {
                // console.log(k, data[i].channels[k])
                const channel = document.createElement('div')
                const parentLabel = document.querySelector(`div[data-label-id='${data[i].channels[k].labelId}']`)
                channel.setAttribute('class', 'channel')
                channel.setAttribute('data-channel-channel-id', data[i].channels[k].channelId)
                channel.setAttribute('data-channel-description', data[i].channels[k].description)
                channel.setAttribute('data-channel-display-name', data[i].channels[k].displayName)
                channel.setAttribute('data-channel-follower', data[i].channels[k].follower)
                channel.setAttribute('data-channel-label-id', data[i].channels[k].labelId)
                channel.setAttribute('data-channel-id', data[i].channels[k].id)
                channel.setAttribute('data-channel-name', data[i].channels[k].name)
                channel.setAttribute('data-channel-profileImg', data[i].channels[k].profileImg)
                channel.setAttribute('data-channel-provider', data[i].channels[k].provider)
                channel.setAttribute('data-channel-subscriber', data[i].channels[k].subscriber)
                channel.setAttribute('draggable', 'true')
                const img = document.createElement('img')

                img.setAttribute('src', data[i].channels[k].profileImg)
                img.style.width = '34px'
                img.style.borderRadius = '17px'
                channel.appendChild(img)
                // channel.appendChild(
                //     document.createTextNode(
                //         `${data[i].channels[k].channelId}, ${data[i].channels[k].provider}`
                //     )
                // )
                parentLabel.appendChild(channel)
                channel.addEventListener('dragstart', (e) => {
                    console.log('dragstart', channel)
                    e.dataTransfer.setData('targetId', channel.dataset.channelId)
                })
            }
        }
        const childElement = document.getElementsByClassName('child-label')
        if (childElement !== null) {
            for (let i = 0; i < childElement.length; i++) {
                childElement[i].style.paddingRight = '8px'
                childElement[i].style.paddingLeft = '32px'
                childElement[i].style.paddingTop = '8px'
                childElement[i].style.paddingBottom = '8px'
            }
        }
        const childElement2 = document.getElementsByClassName('child-label2')
        if (childElement2.length !== 0) {
            for (let i = 0; i < childElement2.length; i++) {
                childElement2[i].style.paddingRight = '8px'
                childElement2[i].style.paddingLeft = Number(childElement[i].dataset.superId) * 16 + 'px'
            }
        }
        const dropCapElement = document.getElementsByClassName('drop-cap')
        for (let i = 0; i < dropCapElement.length; i++) {
            dropCapElement[i].addEventListener('mouseover', () => {
                dropCapElement[i].classList.add('add-shadow')
                dropCapElement[i].parentNode.children[1].classList.add('expand-list')
            })
            dropCapElement[i].addEventListener('mouseleave', () => {
                dropCapElement[i].classList.remove('add-shadow')
                dropCapElement[i].parentNode.children[1].classList.remove('expand-list')
            })
            dropCapElement[i].parentNode.addEventListener('mouseover', () => {
                dropCapElement[i].parentNode.children[1].classList.add('expand-list')
            })
            dropCapElement[i].parentNode.addEventListener('mouseleave', () => {
                dropCapElement[i].parentNode.children[1].classList.remove('expand-list')
            })
        }
        const labelTitleElement = document.getElementsByClassName('label-title')
        for (let i = 0; i < labelTitleElement.length; i++) {
            labelTitleElement[i].addEventListener('mouseover', () => {})
        }
    },
    methods: {
        ...mapGetters({ getJwt: 'login/getJwt' }),
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
