'use client'
import axios from 'axios';
import React, {useState} from "react";
import { z } from 'zod';
import { useRouter } from 'next/navigation';

export default function SignUp() {
    const router = useRouter();
    const labels = ['name', 'nickName', 'email', 'phoneNumber', 'password', 'gender'];
    const [name, setName] = useState("");
    const [text, setText] = useState('sign');
    const [color, setColor] = useState('black');
    const [values, setValues] = useState({});
    const [errorMessage, setErrorMessage] = useState('');

    const fieldSchemas = {
        name: z.string().min(1,'Name must not be empty').max(20,"it's too long"),
        nickName: z.string().min(1,'NickName must not be empty').max(20,"it's too long"),
        email: z.string().email(),
        phoneNumber: z.string().refine(value => /^\d{3}-\d{4}-\d{4}$/.test(value), 'Invalid PhoneNumber'),
        password: z.string().min(8,"Password must be between 8 and 20 characters").max(20,"Password must be between 8 and 20 characters"),
        gender: z.enum(['MALE', 'FEMALE']),
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
        const result = schema.safeParse(values);



        if (!result.success) {
            result.error.errors.forEach((error) => {
                if (error.message === 'Required') {
                    alert(`You should fill in the ${error.path[0]} part first.`);
                } else {
                    alert(error.message);
                }
            });
            return;
        }

        axios.post(`${process.env.NEXT_PUBLIC_API_URL}/users`, values)
            .then(async response => {
                alert(`Your registration has been successfully completed. Welcome,${name}!`);
                await router.push('/log-in');
            })
            .catch(error => {
                alert(error.response.data.message)
            });

    }
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const name = event.target.name as keyof typeof fieldSchemas;
        const value = event.target.value;
        if(name == "name"){
            setName(value);
        }

        try {
            fieldSchemas[name].parse(value);
             setValues({
                ...values,
                [name]: value,
            });
            setErrorMessage('');
        } catch (error) {
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
        <div style={{ position: 'relative', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <img src="/signInPaper.png" alt="image" style={{ width: '80vw', maxWidth: '1000px', height: '80vh', maxHeight: '2000px', marginTop: '10px'}} />
            <div style={{ position: 'absolute', display: 'flex', flexDirection: 'column', alignItems: 'flex-end', justifyContent: 'space-between', minHeight: '20vw' }}>
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center',maxHeight:'50vh' }}>
                    <div>
                        <h1 style={{fontSize: '2vw', fontFamily:'Pacifico, cursive',marginTop :'0.1vw'}}>Registration Form </h1>
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                        <div>
                            {labels.map((label, index) => (
                                <div key={index}>
                                    <h2 className={"signInText"}>{label.charAt(0).toUpperCase() + label.slice(1)}:</h2>
                                </div>
                            ))}
                        </div>
                        <div>
                            <input
                                type="text"
                                name="name"
                                placeholder="Name"
                                onChange={handleChange}
                                className={"signInInput"}
                                onBlur={() => setErrorMessage("")}
                            />

                            {labels.slice(1, 5).map((label, index) => (
                                <div key={index}>
                                    <input
                                        type="text"
                                        name={label}
                                        placeholder={label.charAt(0).toUpperCase() + label.slice(1)}
                                        onChange={handleChange}
                                        className={"signInInput"}
                                        onBlur={() => setErrorMessage("")}
                                    />
                                </div>
                            ))}

                            <input
                                type="text"
                                name="gender"
                                placeholder="MALE or FEMALE"
                                onChange={handleChange}
                                className={"signInInput"}
                                onBlur={() => setErrorMessage("")}
                            />
                        </div>
                    </div>
                    <h1 style={{ fontSize: '20px', fontFamily: 'Pacifico, cursive', marginTop: '30px',color: 'red' }}>{errorMessage}</h1>
                </div>

                <div>
                    <h1 style={{ fontSize: '2vw', fontFamily: 'Pacifico, cursive', marginTop: '50px',color:color }}
                        onClick={handleButtonClick}
                        onMouseDown={handleMouseDown}
                        onMouseUp={handleMouseUp}
                    >
                        {text}
                    </h1>
                </div>

            </div>


        </div>

        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet"/>
    </div>
    )
}