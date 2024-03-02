'use client'
import React, { useEffect ,useState} from 'react';
import {MyResponsiveRadar} from '../nivo/radar'
import axios from 'axios';
import {SyncLoader, ClimbingBoxLoader } from 'react-spinners';


export default function My() {
    const [user, setUser] = useState<UserData | null>(null);

    interface UserData {
        name: string;
        email: string;
        nickName: string;
        phoneNumber: string;
        gender: string;
        genres: Genre[];
    }
    interface Genre {
        genre: string;
        searched: number;
        loaned: number;
    }
    interface ResponseData {
        data: UserData;
    }
    const [genres, setGenres] = useState<Genre[]>([]);

    useEffect(() => {
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/users/my`, {
                headers: { "Authorization": "Bearer " + localStorage.getItem('accessToken') ,
                withCredentials: true 
                }
            })
            .then((response) => {
                const userData = response.data.data;

                const user = {
                    id: userData.id,
                    name: userData.name,
                    email: userData.email,
                    nickName: userData.nickName,
                    phoneNumber: userData.phoneNumber,
                    gender:userData.gender
                };

                const genres = userData.genres.map((genre: Genre) => ({
                    genre: genre.genre,
                    searched: genre.searched,
                    loaned: genre.loaned
                }));
               if(user) { // @ts-ignore
                   setUser(user);
               }
                setGenres(genres);
            })
            .catch((error) => {
                console.error(error);
            });
    }, []);

    const chartData = genres.map((genre: Genre) => ({
        genre: genre.genre,
        searched: genre.searched,
        loaned: genre.loaned
    }));

    return (
        <div style={{ alignItems:'center', display: 'flex', justifyContent:'center'}}>
            <div className="myInfo" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                {user ? (
                        <>
                            <div className="myInfoTextBox">
                                <h2>Welcome, {user.name } !</h2>
                                <p>email: {user.email}</p>
                                <p>nick name: {user.nickName}</p>
                                <p>phone number: {user.phoneNumber}</p>
                                <p>gender: {user.gender}</p>
                            </div>
                            <div className="radar">
                                <MyResponsiveRadar data={chartData} />
                            </div>
                        </>
                    )
                                    :
                    (
                    <>
                        <div className="myInfoTextBox" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center',marginTop: '50px'}}>
                            <SyncLoader size={50} color="darkgoldenrod" />
                        </div>
                        <div className="radar" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center',marginTop: '50px'}}>
                            <ClimbingBoxLoader color="darkgoldenrod" size={50}/>
                        </div>
                    </>
                    )}
            </div>
            <video className="background-video" autoPlay muted loop>
                <source src="/backgroundMy.mp4" type="video/mp4" />
            </video>
        </div>
    );

}

