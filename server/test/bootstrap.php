<?php
// Load the different Functions
foreach (glob("../src/*.php") as $filename)
{
    require_once $filename;
}

foreach (glob("../src/*/*.php") as $filename)
{
    require_once $filename;
}
