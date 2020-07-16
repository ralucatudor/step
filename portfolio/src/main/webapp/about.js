getInfo();

function getInfo() {
    const wrapper = document.getElementById("wrapper");
    wrapper.innerHTML = `
        <h1>About Me</h1> 
        <p>
            <span id="hello-world-greeting">Hello World!</span> My name is Raluca Tudor, and I just finished my first year of studies 
            towards a BSc degree in Computer Science. I am an enthusiastic, persevering and meticulous CS Student, 
            longing to acquire as much knowledge as possible.
        </p>

        <section id="education">
            <h1>Education</h1>
            <section>
                <header>
                    <h1>
                        Faculty of Mathematics and Computer Science,<br>
                        University of Bucharest
                    </h1>
                    <span class="time-span">Oct. 2019 - June 2022</span>
                    <div>BSc in Computer Science</div>
                </header>
                <p>
                    I am the Student Representative of the Olympics' Class of 29 students, including myself. 
                </p>
            </section>
            <section>
                <header>
                    <h1>
                        "Tudor Vianu" National College of Computer Science,<br> 
                        Bucharest
                    </h1>
                    <span class="time-span">Sept. 2015 - June 2019</span>
                    <div>High School</div>
                </header>
                <p>
                    GPA (9<sup>th</sup>-12<sup>th</sup> grade): 9.93/10
                </p>
            </section>
        </section>

        <section id="experience">
            <h1>Experience</h1>
            <section>
                <header>
                    <h1>Student Training in Engineering Program [STEP] Intern at Google</h1>
                    <span class="time-span">July 6 - Sept. 25, 2020</span>
                </header>
                <p>
                    Currently attending trainings, working on some Open source projects, meeting the best Googlers, and more :)
                </p>
            </section>
        </section>

        <section id="volunteering">
            <h1>Volunteering</h1>
            <section>
                <header>
                    <h1>CoderDojo Mentor</h1>
                    <span class="time-span">
                        Sept. 2017 - June 2018 & Sept. 2019 - present
                    </span>
                    </header>
                    <ul>
                        <li>Held programming workshops for pupils;</li>
                        <li>Acquired lesson-planning and teaching skills;</li> 
                        <li>Used feedback as a tool for self-improvement;</li>
                    </ul>
            </section>
        </section>

        <section id="hobbies">
            <h1>Hobbies</h1>
            <section>
                <h1>Sports</h1>
                <ul>
                    <li>Indoor Cycling</li>
                    <li>Dancing</li>
                    <li>Pilates</li>                                  
                </ul>
            </section>
            <section>
                <h1>Reading</h1>
                <p>
                    My favorite book is "Lust for Life" by Irving Stone.
                </p>
            </section>
        </section>
    `;
}