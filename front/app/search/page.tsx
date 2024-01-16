'use client'
import React, {useEffect, useState} from "react";
import axios from 'axios';
import { useSearchParams } from "next/navigation";


export default function Search() {
    const [selected, setStr] = useState("TITLE");
    const [isClicked, setIsClicked] = useState(false);
    const [url, setUrl] = useState("keyword");
    const [input, setInput] = useState("");
    const [selectedBook, setSelectedBook] = useState<Book | null>(null);
    const params = useSearchParams();
    const id = params.get('id');
    const [message, setMessage] = useState("Type here");

    interface Book {
        id: string;
        title: string;
        imageURL: string;
        publisher: string;
        author : string;
        status : string;
        libraryName : string;
    }
    const [books, setBooks] = useState<Book[]>([]);
    useEffect(() => {
        async function fetchData() {
            if(id) {
                const response = await axios.get(`http://localhost:8080/books/${id}`);
                setSelectedBook(response.data.data);
                setMessage(response.data.data.title);
            }
        }
        fetchData();
    }, [])
    const handleTitleClick = () => {
        setStr("TITLE");
        setUrl("keyword")
    };

    const handleAuthorClick = () => {
        setStr("AUTHOR");
        setUrl("author")
    };

    const handlePublisherClick = () => {
        setStr("PUBLISHER");
        setUrl("publisher")
    };
    const handleMouseDown = () => {
        setIsClicked(true);
    };

    const handleMouseUp = () => {
        setIsClicked(false);
    };
    const handleButtonClick = async () => {

        try {
            const response = await axios.get(`http://localhost:8080/books/${url}/${input}`)
            const dataArray = response.data.data;
            const books = dataArray.map((item: { id: string; title: string; imageURL: string, publisher: string, author : string, status : string, libraryName : string}) => ({
                id: item.id,
                title: item.title,
                imageURL: item.imageURL,
                publisher:  item.publisher,
                author: item.author,
                status: item.status,
                libraryName: item.libraryName
            }));
            setBooks(books);
        }
        catch(error){
            console.error('Error in axios.get', error);
        }
    };
    const handleInputChange = (event:React.ChangeEvent<HTMLInputElement>) => {
        setInput(event.target.value);
    };
    const handleTextClick = (book:Book) => {
        setSelectedBook(book);
    }
    return (
        <div>

            <div className="smallTitle">
                <h4>search</h4>
            </div>

            <video className="background-video" autoPlay muted loop>
                <source src="/backgroundSearch.mp4" type="video/mp4" />
            </video>

            <div className="search-container">
                <h2 style={{ float: 'left', fontFamily: 'Long Cang, cursive', fontSize: '2vw',color: 'papayawhip',marginLeft: '10vw',marginRight: '30px' }} className="search-label">Search by</h2>

                <div className="buttons" style={{float:'left',marginRight: '30px'}}>
                    <button className="title-button" onClick={handleTitleClick}>Title</button>
                    <div></div>
                    <button className="author-button" onClick={handleAuthorClick}>Author</button>
                    <div></div>
                    <button className="publisher-button" onClick={handlePublisherClick}>Publisher</button>
                </div>
                <div className="selected-wrapper" style={{ float: 'left', width: '13vw',justifyContent: 'center',alignItems: 'center', height: '5vw',marginTop: '20px'}}>
                    <div className="selected" style={{ float: 'left', fontFamily: 'Nanum Pen Script',marginRight: '50px',textAlign: 'center',marginTop: '10px',fontSize: '3vw',color:'gold' }}>
                        <div>{selected}</div>
                    </div>
                </div>
                <div className="search" style={{float:'left',marginRight: '30px',marginTop: '42px'}}>
                    <input type="text" placeholder={message} style={{ width: '35vw', height: '2.5vw',fontSize: '2vw' }} onChange={handleInputChange} />
                </div>
            </div>
            <img src="/search.gif" alt="image"
                 onClick={handleButtonClick}
                 className={isClicked ? 'searchImage clicked' : 'searchImage'}
                 onMouseDown={handleMouseDown}
                 onMouseUp={handleMouseUp}
                 onMouseLeave={handleMouseUp}/>
            <div className="searchResult">
                {books.map((book, index) => (
                    <div key={index} className="searchResultText" onClick={() => handleTextClick(book)}>{book.title}</div>
                ))}
            </div>
            <div className="bookDetail">
                {selectedBook ? (
                    <div className="bookDetailContent">
                        <img src={selectedBook.imageURL} alt={selectedBook.title} className="book-image-big"/>
                        <div className="searchInfo">
                            <h2 >{selectedBook.title }</h2>
                            <p>저자: {selectedBook.author}</p>
                            <p>출판사: {selectedBook.publisher}</p>
                            <p>상태: {selectedBook.status}</p>
                            <p>소장 도서관: {selectedBook.libraryName}</p>
                        </div>
                    </div>
                ) : (
                    <div></div>
                )}
            </div>
        </div>

    );
}