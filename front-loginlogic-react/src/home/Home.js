import React, { Component } from 'react';
import './Home.css';
import { youtubeAPI } from '../util/APIUtils';
import Alert from 'react-s-alert';

class Home extends Component {
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
    render() {
        return (
            <div className="home-container">
                <div className="container">
                    <h1 className="home-title">Demo project</h1>

                </div>
                <div>
                    <button onClick={this.handleYoutubeButton}>
                        youtubeAPI Testbutton
                    </button>
                </div>
            </div>
        )
    }
}

export default Home;