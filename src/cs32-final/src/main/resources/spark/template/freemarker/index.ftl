
<br><br>
<div class="container">
  <h1>Welcome to YELP 2.0</h1>
  <p>Choose a place to eat for your family and friends!</p> 
</div>

<div ng-app="coolform" class="wrapper">
  
  <div cool-form>
    <div class="q-title">Create a poll!</div>
    <div ng-repeat="q in questions" class="question">
      <label>
        <span class="q-text">
          {{q.question}}
          <span class="q-answer" id="q-answer">
            {{q.answer}}
          </span>
        </span>
        <input class="input-form" type="text" id="q{{$index}}" ng-model="q.answer">
        <span class="q-back" ng-click="open($index)">&lt;</span>
      </label>
      <span class="q-next" ng-click="open($index+1)">&#x21E8;</span>  
    </div>
    <div class="q-after">
      <hr>
      <div class="q-confirm-text">Is the above information correct?</div>
      <a href="/poll"><div class="q-confirm-button">Absolutely</div></a>
    </div>
  </div>
  <center ng-show="activequestion > -1 && activequestion < questions.length">{{activequestion+1}} / {{questions.length}}</center>
  
</div>



    
<#include "main.ftl">