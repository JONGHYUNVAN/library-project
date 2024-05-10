import Link from 'next/link';


export default function Home() {
  let name = 'J.H.VAN';
  return (
      <>
          <head>
              <title> Welcome! </title>
              <meta name="google-site-verification" content="0JhXiQkUPWSHrMe_ZNDvxTZM1CNrIKsLyczs8e_EWuc"/>
          </head>

          <div className="main-page">
              <div className="title" >
                  <Link href="/ranking" className="titleLetter">Library project</Link>
              </div>
                  <Link className="title-sub" href="/cursor">by {name}</Link>

              <Link href="/ranking" className="cursor">
              <video poster={"/backposter.png"} className="background-video" autoPlay muted loop >
                  <source src="/background.mp4" type="video/mp4" />
                </video>
              </Link>
          </div>

      </>
  );
}
