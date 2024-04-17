'use client'
import axios from 'axios';
import React, {useState} from "react";
import { z } from 'zod';
import { useRouter } from 'next/navigation';
import Image from 'next/image'


export default function EditProfile() {
    const router = useRouter();
    const labels = [ 'nickName', 'phoneNumber', 'password', 'gender'];
    const [name, setName] = useState("");
    const [profile, setProfile] = useState(0);
    const [text, setText] = useState('sign');
    const [color, setColor] = useState('black');
    const [values, setValues] = useState({});
    const [errorMessage, setErrorMessage] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const [hasError, setHasError] = useState(false);
    const fieldSchemas = {
        nickName: z.string().max(20,"it's too long").nullable(),
        phoneNumber: z.string().refine(value => /^\d{3}-\d{4}-\d{4}$/.test(value), 'Invalid PhoneNumber').nullable(),
        gender: z.enum(['MALE', 'FEMALE']),
        profile: z.number().nullable()
    };
    const schema = z.object(fieldSchemas);

    const handleMouseDown = () => {
        if(name) setColor('red');
        if(name) setText(name);
    };

    const handleMouseUp = () => {
        setColor('black');
        setText("sign")
    };

    const handleButtonClick = () => {
        const filteredValues = Object.fromEntries(Object.entries(values).filter(([key, value]) => value !== ''));
        const userData = {
            ...filteredValues,
            profile
        };
        if('gender' in userData) {
            setSubmitting(true);
            axios.patch(`${process.env.NEXT_PUBLIC_API_URL}/users/me`, userData,{
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                }
            })
                .then(async response => {
                    alert(`Your profile update has been successfully completed. Welcome,${name}!`);
                    await router.push('/my');
                })
                .catch(error => {
                    alert(error.response.data.message)
                })
                .finally(() => {
                    setSubmitting(false);
                });
        }
        else {
            alert('Gender Must be contained');
            setHasError(true);
        }

    }
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const name = event.target.name as keyof typeof fieldSchemas;
        const value = event.target.value;

        try {
            fieldSchemas[name].parse(value);
             setValues({
                ...values,
                [name]: value,
            });
            setHasError(false);
            setErrorMessage('');
        } catch (error) {
            setHasError(true);
            if (error instanceof z.ZodError) {
                if(name === 'gender') setErrorMessage("Gender should be 'Male' or 'Female'")
                else setErrorMessage(error.errors[0].message);
            }
        }
    };

    return (
    <div>
        <video className="background-video" autoPlay muted loop>
            <source src="/backgroundSignIn.mp4" type="video/mp4" />
        </video>
        {!profile ?
            (<div className="profileSelect">
                <h2 className="chooseProfileText" >choose your character</h2>
                <Image src="/profile1.jpg" className="profileImage" alt="image" width={200} height={2000} onClick={() => setProfile(1)} />
                <Image src="/profile2.jpg" className="profileImage" alt="image" width={200} height={2000} onClick={() => setProfile(2)} />
                <Image src="/profile3.jpg" className="profileImage" alt="image" width={200} height={2000} onClick={() => setProfile(3)} />
                <Image src="/profile4.jpg" className="profileImage" alt="image" width={200} height={2000} onClick={() => setProfile(4)} />
                <Image src="/profile5.jpg" className="profileImage" alt="image" width={200} height={2000} onClick={() => setProfile(5)} />
            </div>)

            : (<div style={{position: 'relative', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>

                <Image src="/signInPaper.png" alt="image" width={1000} height={2000} style={{
                    width: 'clamp(500px, 80vw, 1000px)',
                    height: 'clamp(600px, 80vh, 2000px)',
                    marginTop: '1vh'
                }}/>
                <div style={{
                    position: 'absolute',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'flex-start',
                    justifyContent: 'space-between'
                }}>
                    <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', maxHeight: '50vh'}}>
                        <div>
                            <h1 style={{
                                position: 'absolute',
                                top: '-15%',
                                left: '35%',
                                fontSize: 'clamp(20px, 2vw, 40px)',
                                fontFamily: 'Pacifico, cursive'
                            }}>Registration Form </h1>
                        </div>

                        <div style={{display: 'flex', justifyContent: 'space-between', width: '100%'}}>
                            <div>
                                {labels.map((label, index) => (
                                    <div key={index}>
                                        <h2 className={"signInText"}>{label.charAt(0).toUpperCase() + label.slice(1)}:</h2>
                                    </div>
                                ))}
                            </div>
                            <div>
                                {labels.slice(0, 3).map((label, index) => (
                                    <div key={index}>
                                        <input
                                            type="text"
                                            name={label}
                                            placeholder={'If unwanted, leave it blank'}
                                            onChange={handleChange}
                                            className="signInInput"
                                            onBlur={() => setErrorMessage("")}
                                        />
                                    </div>
                                ))}

                                <input
                                    type="text"
                                    name="gender"
                                    placeholder="Must be contained, MALE or FEMALE"
                                    onChange={handleChange}
                                    className={"signInInput"}
                                    onBlur={() => setErrorMessage("")}
                                />
                            </div>
                        </div>
                        <h1 style={{
                            fontSize: '20px',
                            fontFamily: 'Pacifico, cursive',
                            marginTop: '2vh',
                            color: 'red'
                        }}>{errorMessage}</h1>
                    </div>

                    <div>
                        <h1 style={{
                            position: 'absolute',
                            fontSize: 'clamp(20px, 2vw, 40px)',
                            fontFamily: 'Pacifico, cursive',
                            color: color,
                            bottom: '-10%',
                            right: '-5%',
                            transform: 'translate(-50%, -50%)'
                        }}
                            onClick={submitting ? () => {
                            } : handleButtonClick}
                            onMouseDown={handleMouseDown}
                            onMouseUp={handleMouseUp}
                        >
                            { !hasError ?  (`${text}`):(``) }
                        </h1>
                    </div>
                </div>

            </div>)
        }


    </div>

    )
}