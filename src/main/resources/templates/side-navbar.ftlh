<nav style="height: 100vh; width: 220px;">
    <div class="d-flex flex-column flex-shrink-1 p-3 text-white bg-dark" style="height: 100%;">
        <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <img src="/static/img/logo.png" style="clip-path: circle(31%); width: 190px"/>
        </a>
        <hr>
        <ul class="nav nav-pills flex-column mb-auto">
            <li class="nav-item">
                <a href="/order-restaurant" class="nav-link" aria-current="page">
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
        <div class="dropdown">
            <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                <img src="https://gravatar.com/avatar/effc5bf46ccf80b7608cc96b4feea54d4fcfddaa3d7cab4a2f936c7f81369110" alt="" width="32" height="32" class="rounded-circle me-2">
                <strong>jkowalski</strong>
            </a>
            <ul class="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
                <li><a class="dropdown-item" href="#">New project...</a></li>
                <li><a class="dropdown-item" href="#">Settings</a></li>
                <li><a class="dropdown-item" href="#">Profile</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="#">Sign out</a></li>
            </ul>
        </div>
    </div>
</nav>