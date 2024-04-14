'use client'
import React, {useEffect, useState} from "react";
import axios from 'axios';
import { useRouter } from 'next/navigation';
import { useSearchParams } from "next/navigation";
import {Post} from '../interface/Post'
import {BookImage} from  '../interface/Post'
import { z } from 'zod';

export default function Write() {
    const router = useRouter();
    const params = useSearchParams();
    const [id, setId] = useState<string | null>(params.get('id'));
    const [post, setPost] = useState<Post | null>(null);
    const [book, setBook] = useState<BookImage | null>(null);
    const [errorMessage, setErrorMessage] = useState('');
    const [values, setValues] = useState({});
    const [submitting, setSubmitting] = useState(false);
    const [checked, setChecked] = useState(false);


    const fieldSchemas = {
        title: z.string().min(1,'Title must not be empty').max(30,"it's too long"),
        content: z.string().min(1,'Content must not be empty'),
    };
    const schema = z.object(fieldSchemas);

    useEffect(() => {
        async function fetchData() {
            const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/books/${id}`);
            const dataArray = response.data.data;
            setBook(dataArray);
        }
        fetchData();
    }, [id])
    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const name = event.target.name;
        const value = event.target.value;
        setValues((currentValues) => ({
            ...currentValues,
            [name]: value,
        }));
        try {
            const schema = fieldSchemas[name as keyof typeof fieldSchemas];
            schema.parse(value);
            setErrorMessage('');
        } catch (error) {
            if (error instanceof z.ZodError) setErrorMessage(error.errors[0].message);
        }
    };
    const checkBook = () => {
        setChecked(true);
        if (!id && !checked) {
            alert(`You should select the book first`);
            router.push(`/search`);
        }
    }
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
        const postData = {
            ...values,
        };

        setSubmitting(true);
        axios.post(`${process.env.NEXT_PUBLIC_API_URL}/posts`, postData)
            .then(async response => {
                router.push('/posts');
            })
            .catch(error => {
                alert(error.response.data.message)
            })
            .finally(() => {
                setSubmitting(false);
            });



    }
    return (
    <div>
        <video className="background-video" autoPlay muted loop>
            <source src="/write.mp4" type="video/mp4" />
        </video>

                <div className="post">
                    <span className="writeTitleBox">
                        <input
                            type="text"
                            name="title"
                            placeholder="Write your title here"
                            onChange={handleChange}
                            className={"writeTitle"}
                            onFocus={(e) => {
                                checkBook();
                                e.target.placeholder = "";
                            }}
                            onBlur={(e) => {
                                e.target.placeholder = "Write your title here";
                                setErrorMessage("");
                            }}
                        />
                    </span>

                    <div className="wtiteContent">
                        <textarea
                            name="content"
                            placeholder="Write your thoughts here."
                            className={"writeContent"}
                            onChange={handleChange}
                            onFocus={(e) => {
                                checkBook();
                                e.target.placeholder = "";
                            }}
                            onBlur={(e) => {
                                e.target.placeholder = "Write your thoughts here.";
                                setErrorMessage("");
                            }}
                        />
                    </div>
                    <div className="postButton"
                         onClick={submitting ? () => {
                         } : handleButtonClick}>
                        Post
                    </div>
                    {book ? (
                        <div className="postBook">
                            <img src={book.imageURL}
                                 alt={"book"}
                                 className="postImage"
                                 onClick={(e) => {
                                     if (window.confirm("Your written content will be deleted. Do you wish to continue?")) {
                                         router.push(`/search/?id=${book.id}`);
                                     }
                                 }}
                            />

                            <span className="postBookTitle">
                          {book.title}
                        </span>
                        </div>
                    ) : (<></>)}
                    <div className="postTimes">
                    </div>

                </div>
        )


    </div>

    )
}