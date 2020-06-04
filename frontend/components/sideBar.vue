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
        this.labels = (
            await this.$axios.get('https://k02d1031.p.ssafy.io:8081/v1/member/information', {
                headers: { Authorization: 'Bearer ' + this.getJwt() },
            })
        ).data.label
        localStorage.setItem('labels', JSON.stringify(this.labels))
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

            if (data[i].superId === -1) {
                node.setAttribute('id', 'label-wrap')
                const span = document.createElement('span')
                span.appendChild(document.createTextNode('카테고리'))
                span.setAttribute('id', 'root-label')
                node.appendChild(span)
                tree.appendChild(node)
            } else {
                const parentLabel = document.querySelector(`div[data-label-id='${data[i].superId}']`)
                if (data[i].superId === 1) {
                    const span = document.createElement('span')
                    const wrap = document.createElement('div')
                    const hr = document.createElement('hr')
                    wrap.setAttribute('class', 'drop-cap-wrap')
                    span.setAttribute('class', 'drop-cap')
                    span.appendChild(document.createTextNode(data[i].labelName[0]))
                    wrap.appendChild(span)
                    wrap.appendChild(node)
                    node.appendChild(document.createTextNode(data[i].labelName))
                    node.appendChild(hr)
                    node.setAttribute('class', 'child-label')
                    parentLabel.insertBefore(wrap, document.querySelector('.channel'))
                } else {
                    const hr = document.createElement('hr')
                    node.appendChild(document.createTextNode(data[i].labelName))
                    node.appendChild(hr)
                    node.setAttribute('class', 'child-label')
                    parentLabel.insertBefore(node, document.querySelector('.channel'))
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
            }
        }
        const childElement = document.getElementsByClassName('child-label')
        if (childElement.length !== 0) {
            childElement[0].style.paddingRight = '8px'
            for (let i = 0; i < childElement.length; i++) {
                childElement[i].style.paddingLeft = Number(childElement[i].dataset.superId) * 12 + 'px'
            }
        }
        const dropCapElement = document.getElementsByClassName('drop-cap-wrap')
        if (dropCapElement.length !== 0) {
            dropCapElement[0].addEventListener('mouseover', () => {
                dropCapElement[0].classList.add('expand-list')
                dropCapElement[0].children[1].classList.add('add-border')
            })
            dropCapElement[0].addEventListener('mouseleave', () => {
                dropCapElement[0].classList.remove('expand-list')
                dropCapElement[0].children[1].classList.remove('add-border')
            })
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
