# 디자인패턴 공부

## MVC 패턴 구현

- build gradle에 dataBinding 선언
- xml에 data 추가 → 전체를 layout으로 감 싸고 그 안에 <data>태그

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="mainActivity"
            type="com.grusie.mvvmpractice.MainActivity" />
    </data>
```

- 컨트롤러에 데이터 바인딩 연결

```kotlin
lateinit var binding : ActivityMainBinding

binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
binding.mainActivity = this
```

※ binding.invalidateAll() : data가 변한 후 연결된 view들에 변화를 알려주는 함수(텍스트 값을 변화시키거나 할 때 사용)

- 모델 클래스 생성

```kotlin
package com.grusie.mvvmpractice

class Model {
    val password : MutableList<Int> = mutableListOf()

    fun inputPassword(i : Int){
        if(password.size < 4){
            password.add(i)
        }
    }

    fun checkPassword() : Boolean{
        var trueCount = 0
        var savePassword = mutableListOf<Int>(1,2,3,4)  //데이터베이스 대체

        for(i in 0 until savePassword.size) {
            if(savePassword[i] == password[i]){
                trueCount++
            }
        }
        return trueCount==4
    }
}
```

사용자가 누른 비밀번호와 데이터베이스에 있는 비밀번호를 비교해주는 Model 클래스

```kotlin
fun clickNum(i: Int){
        Toast.makeText(this, "$i 버튼이 클릭됨", Toast.LENGTH_SHORT).show()

        model.inputPassword(i)
        if(model.password.size == 4 && model.checkPassword()){
            binding.messageSuccess.visibility = View.VISIBLE
        }
    }
```

모델 클래스를 사용하여, 비밀번호를 체크하고 맞다면, 뷰를 변경함

### 정리

- 사용자의 입력을 받은 액티비티가 모델과 비교하여 뷰를 변경함

## MVP패턴 구현

- Presenter 클래스 생성
- ViewInterface 인터페이스 생성

```kotlin
package com.grusie.mvvmpractice

interface ViewInterface {
    fun ToastMessage(i:Int)
    fun checkPasswordMessage()
}
```

- 액티비티에서 ViewInterface 상속

```kotlin
class MainActivity : AppCompatActivity(),ViewInterface
```

- Presenter클래스는 Model 클래스를 가지고 있어야함
- 액티비티에서 Presenter클래스를 인터페이스를 매개변수로 가진 생성자로 선언하고 이벤트가 발생했을 때 Presenter클래스로 넘겨줌

```kotlin
var presenter = Presenter(this)

...

fun clickNum(i: Int){
        presenter.clickNum(i)
    }
```

- Presenter 클래스 구현 부분

```kotlin
package com.grusie.mvvmpractice

class Presenter(var viewInterface: ViewInterface) {
    val model = Model()

    fun clickNum(i: Int){
        viewInterface.ToastMessage(i)

        model.inputPassword(i)
        if(model.password.size == 4 && model.checkPassword()){
            viewInterface.checkPasswordMessage()
        }
    }
}
```

- ViewInterface를 매개변수로 가지는 클래스로 구현
- 동작해야할 코드들을 인터페이스로 호출하여 사용

### 정리

- 사용자의 액션을 액티비티(뷰)로 받아서 Presenter에 넘겨주고 Presenter가 Model을 비교/가공하여, 인터페이스를 통해 액티비티(뷰)를 변경하도록 되돌려줌

## MVVM 패턴 구현

- ViewModel 클래스 구현

```kotlin
package com.grusie.mvvmpractice

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData

class ViewModel {
    var toastMessage = MutableLiveData<Int>()
    var checkPasswordMessage = ObservableField<Boolean>(false)

    val model = Model()

    fun clickNum(i: Int){
        toastMessage.value = i

        model.inputPassword(i)
        if(model.password.size == 4 && model.checkPassword()){
            checkPasswordMessage.set(true)
        }
    }
}
```

- MVP에서 만든 인터페이스 부분을 MutableLiveData, ObservableField를 사용하여 변수로 만든다
- presenter클래스에 있던 코드들을 ViewModel로 옮김

- xml에 데이터 바인딩 부분 수정

```kotlin
<data>
        <import type="android.view.View"/>
        <variable
            name="viewModel"
            type="com.grusie.mvvmpractice.ViewModel" />
    </data>
```

- viewModel을 사용할 것이기 때문

- 메인액티비티 수정

```kotlin
var viewModel = ViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.viewModel = viewModel
        viewModel.toastMessage.observe(this, Observer {
            Toast.makeText(this, "$it 버튼이 클릭됨", Toast.LENGTH_SHORT).show()
        })
        viewModel.checkPasswordMessage.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if(viewModel.checkPasswordMessage.get() == true){
                    binding.messageSuccess.visibility = View.VISIBLE
                }else{
                    binding.messageSuccess.visibility = View.GONE
                }
            }
        })
    }
```

- ViewModel 객체 생성
- LiveData인 toastMessage는 observe를 사용 observableField인 checkPasswordMessage는 addOnPropertyChangedCallback을 사용해서 사용자의 액션이 들어오면 부분

### 정리

- 사용자의 액션을 View에서 받고, ViewModel이 이를 확인 해, Model에게 데이터를 요청하고 다시 View에 띄워줌
