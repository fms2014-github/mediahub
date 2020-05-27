import React, { Component } from 'react';
import './Profile.css';
import { youtubeAPI, twitchAPI, youtubeGetSubscriptions, youtubeGetVideoId, youtubeTest } from '../../util/APIUtils';
import Alert from 'react-s-alert';

class Profile extends Component {
    state = {
        token: 'test',
        channelId: ''
    }
    constructor(props) {
        super(props);
        console.log(props);
    }
    handleYoutubeButton(){
        youtubeAPI()
        .then(response =>{
            console.log(response);
            Alert.success(response);
        }).catch(error => {
            console.log(error);
            //Alert.error(error);
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        })
    }
    handleTwitchButton(){
        twitchAPI()
        .then(response =>{
            console.log(response);
            Alert.success(response);
        }).catch(error => {
            console.log(error);
            //Alert.error(error);
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        })
    }
    handleTokenChange(event){
        this.setState({
            token: event.target.value
        })
    }
    handleChannelIdChange(event){
        this.setState({
            channelId: event.target.value
        })
    }
    handleSubscriptions(){
        console.log(this.state.token);
        youtubeGetSubscriptions(this.state.token)
        .then(response =>{
            console.log(response)
        }).catch(error => {
            console.log(error)
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        })
    }
    youtubeGetVideoId(){
        console.log(this.state.token);
        youtubeGetSubscriptions(this.state.channelId, this.state.token)
        .then(response =>{
            console.log(response)
        }).catch(error => {
            console.log(error)
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        })
    }
    testButton(){
        youtubeTest()
        .then(response =>{
            console.log(response)
        }).catch(error => {
            console.log(error)
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        })
    }
    render() {
        return (
            <div className="profile-container">
                <div className="container">
                    <div className="profile-info">
                        <div className="profile-avatar">
                            { 
                                this.props.currentUser.profileUrl ? (
                                    <img src={this.props.currentUser.profileUrl} alt={this.props.currentUser.name}/>
                                ) : (
                                    <div className="text-avatar">
                                        <span>{this.props.currentUser.name && this.props.currentUser.name[0]}</span>
                                    </div>
                                )
                            }
                        </div>
                        <div className="profile-name">
                           <h2>{this.props.currentUser.name}</h2>
                           <p className="profile-email">{this.props.currentUser.email}</p>
                        </div>
                        <div>
                            <a href="http://localhost:8080/youtube/test"> test </a>
                        </div>
                    </div>
                </div>
                <div>
                    <button onClick={this.handleYoutubeButton}>
                        youtubeAPI Testbutton
                    </button>
                </div>
                <div>
                    <button onClick={this.handleTwitchButton}>
                        twitch Token-url
                    </button>
                </div>
                <div>
                    <input type="text" value={this.state.token} onChange={this.handleTokenChange.bind(this)}></input>
                    <button onClick={this.handleSubscriptions.bind(this)}>youtubeAPI Subscriptions</button>
                </div>
                <div>
                    <input type="text" value={this.state.channelId} onChange={this.handleChannelIdChange.bind(this)}></input>
                    <button onClick={this.youtubeGetVideoId.bind(this)}>youtubeAPI Search</button>
                </div>
                <div>
                    <button onClick={this.testButton}>
                        test button
                    </button>
                </div>
            </div>
        );
    }
}

export default Profile