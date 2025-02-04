<nav style="height: 100vh; width: 220px;" class="position-sticky top-0">
    <div class="d-flex flex-column flex-shrink-1 p-3 text-white bg-dark" style="height: 100vh;">
        <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <img src="/static/img/logo.png" style="clip-path: circle(31%); width: 190px"/>
        </a>
        <hr>
        <ul class="nav nav-pills flex-column mb-auto">
            <li class="nav-item">
                <a href="/orders" class="nav-link" aria-current="page">
                    <i class="bi bi-card-checklist"></i>&nbsp;
                    Zamówienia
                </a>
            </li>
            <li>
                <a href="/restaurant" class="nav-link text-white" id="restaurant-dropdown-link">
                    <i class="bi bi-shop"></i>&nbsp;
                    Restauracje
                </a>
            </li>
            <li>
                <a href="#" class="nav-link text-white">
                    <i class="bi bi-hourglass"></i>&nbsp;
                    Historia
                </a>
            </li>
            <li>
                <a href="#" class="nav-link text-white">
                    <i class="bi bi-people-fill"></i>&nbsp;
                    Organizator
                </a>
            </li>
        </ul>
        <hr>


        <#if currentUser?has_content>
            <div class="dropdown">
                <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                    <img src="https://gravatar.com/avatar/effc5bf46ccf80b7608cc96b4feea54d4fcfddaa3d7cab4a2f936c7f81369110" alt="" width="32" height="32" class="rounded-circle me-2">
                    <strong>${currentUser.firstName}</strong>
                </a>
                <ul class="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
                    <li><a class="dropdown-item" href="#">New project...</a></li>
                    <li><a class="dropdown-item" href="#">Settings</a></li>
                    <li><a class="dropdown-item" href="#">Profile</a></li>
                    <li><hr class="dropdown-divider"></li>

                    <li><a class="dropdown-item" href="/sign-out">Sign out</a></li>
                </ul>
            </div>
        <#else>
            <a href="#" data-bs-toggle="modal" data-bs-target="#sign-in-modal" class="text-white text-decoration-none fw-bold"> Zaloguj się</a>
        </#if>
    </div>
</nav>

<div class="modal fade" id="sign-in-modal" tabindex="-1" aria-labelledby="sign-in-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div id="modal-title-div">
                    <h1 class="modal-title fs-5">ZALOGUJ SIĘ</h1>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id=sign-in-form" method="post" action="/sign-in">
                    <select name="userId">
                        <#list users as user>
                            <option value="${user.userId}">${user.firstName} ${user.lastName}</option>
                        </#list>
                    </select>
                    <button class="btn btn-dark" type="submit">Zaloguj</button>
                </form>
            </div>
            <div class="modal-footer">
                <button id="submit-form-button" class="btn btn-outline-dark mb-3" type="submit">Dodaj</button>
                <button type="button" class="btn btn-secondary mb-3" data-bs-dismiss="modal">Zamknij</button>
            </div>
        </div>
    </div>
</div>