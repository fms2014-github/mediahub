import React, { Component } from 'react';
import './Profile.css';
import { getTwitchCodeURL, getYoutubeURL } from '../../util/APIUtils';
import Alert from 'react-s-alert';

class Profile extends Component {
    state = {
        accessToken: 'test',
        channelId: 'channelId',
        refreshToken: 'refresh'
    }
    constructor(props) {
        super(props);
        console.log(props);
    }
    handlerTwitchCodeUrl(){
        getTwitchCodeURL()
        .then(res=>{
            console.log(res);
        }).catch(err=>{
            console.log(err);
        })
    }
    handlerYoutubeCodeUrl(){
        getYoutubeURL()
        .then(res=>{
            console.log(res);
        }).catch(err=>{
            console.log(err);
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
                    </div>
                </div>
                <div>
                    <button onClick={this.handlerTwitchCodeUrl}> twitch token </button>
                </div>
                <div>
                    <button onClick={this.handlerYoutubeCodeUrl}> youtube token </button>
                </div>
            </div>
        );
    }
}

export default Profile